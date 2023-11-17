package com.hg.interfaceextension.web.filter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.ContentType;
import com.hg.interfaceextension.common.InterfaceCryptoConstants;
import com.hg.interfaceextension.common.entity.AccessDetail;
import com.hg.interfaceextension.common.exception.AccessKeyException;
import com.hg.interfaceextension.common.exception.IPAddressException;
import com.hg.interfaceextension.common.exception.RequestExpiredException;
import com.hg.interfaceextension.common.exception.SignatureException;
import com.hg.interfaceextension.common.service.AccessDetailService;
import com.hg.interfaceextension.configuration.properties.CompleteInterfaceCryptoProperties;
import com.hg.interfaceextension.web.ControlHttpServletRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 接口鉴权过滤器
 *
 * @author huangguang
 */
public class AuthFilter extends OncePerRequestFilter {


    private final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    /**
     * 全局的配置文件
     */
    private CompleteInterfaceCryptoProperties cryptoProperties;

    /**
     * 校验签名时所需的加密工具
     */
    private final MD5 md5 = SecureUtil.md5();

    /**
     * 授权信息查询服务层
     */
    private AccessDetailService accessDetailService;

    /**
     * 全局的异常解析器
     */
    private HandlerExceptionResolver handlerExceptionResolver;

    /**
     * 进行过滤的操作
     *
     * @param request     本次的请求
     * @param response    本次的响应对象
     * @param filterChain 过滤链
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            // 授权参数的断言
            String accessKey = request.getHeader(InterfaceCryptoConstants.ACCESS_KEY_NAME);
            if (!StringUtils.hasText(accessKey)) {
                throw new AccessKeyException();
            }

            // 时间戳参数的断言
            String timestampStr = request.getHeader(InterfaceCryptoConstants.TIMESTAMP_NAME);
            if (!StringUtils.hasText(timestampStr)) {
                throw new IllegalArgumentException("timestamp缺失");
            }
            long timestamp;
            try {
                timestamp = Long.parseLong(timestampStr);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("timestamp错误");
            }
            Integer requestDifferSeconds = cryptoProperties.getRequestDifferSeconds();
            if (requestDifferSeconds != null) {
                long currTimestamp = System.currentTimeMillis();
                if (currTimestamp - timestamp > requestDifferSeconds * 1000) {
                    throw new RequestExpiredException();
                }
            }

            boolean isFirstRequest = !isAsyncDispatch(request);
            HttpServletRequest requestWrapper = request;

            // 对请求体内容参数的请求需要对请求对象做一次包装，否则后续读取参数时会造成Stream closed
            String encryptStr = null;
            String contentType = request.getHeader("Content-Type");
            if (StringUtils.hasText(contentType)) {
                if (contentType.contains(ContentType.JSON.getValue())) {
                    encryptStr = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
                    if (isFirstRequest && !(request instanceof ControlHttpServletRequestWrapper)) {
                        requestWrapper = new ControlHttpServletRequestWrapper(request, StringUtils.hasText(encryptStr) ? encryptStr.getBytes(StandardCharsets.UTF_8) : "".getBytes(StandardCharsets.UTF_8));
                    }
                } else if (contentType.contains(ContentType.FORM_URLENCODED.getValue())
                        || contentType.contains(ContentType.MULTIPART.getValue())) {
                    encryptStr = request.getParameter(cryptoProperties.getRequestParameterName());
                } else {
                    throw new HttpMediaTypeNotSupportedException("不支持的内容类型");
                }
            }

            if (logger.isDebugEnabled()) {
                logger.debug("请求参数加密值：" + encryptStr);
            }

            // 授权码断言
            AccessDetail accessDetail = accessDetailService.loadSysAccessByAccessKey(accessKey);
            if (accessDetail == null) {
                throw new AccessKeyException("授权码未找到");
            }

            // IP地址的断言
            String whiteIPList = accessDetail.getWhiteIPList();
            if (StringUtils.hasText(whiteIPList)) {
                String clientIP = ServletUtil.getClientIP(request);
                String[] IPs = whiteIPList.split(",");
                if (!ArrayUtil.contains(IPs, clientIP)) {
                    throw new IPAddressException();
                }
            }

            /// 对请求参数进行签名
            // 1 参数的拼接
            List<String> itemList = new ArrayList<>();
            if (StringUtils.hasText(encryptStr)) {
                itemList.add(encryptStr);
            }

            // 2 通用参数拼接
            itemList.add("secret_key" + "=" + accessDetail.getSecretKey());
            itemList.add("timestamp" + "=" + timestamp);
            String paramStr = CollUtil.join(itemList, "&");

            // 3 参数值加密
            String sign = md5.digestHex(paramStr, StandardCharsets.UTF_8);
            String provideSign = request.getHeader(InterfaceCryptoConstants.SIGNATURE_NAME);
            if (logger.isDebugEnabled()) {
                logger.debug("签名比对，计算值：{}", sign);
                logger.debug("签名比对，提供值：{}", provideSign);
            }
            if (!StringUtils.hasText(provideSign) || !sign.equals(provideSign)) {
                throw new SignatureException();
            }
            filterChain.doFilter(requestWrapper, response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }

    /**
     * 设置查询的授权服务对象，不调用此对象将会出现NullPointerException
     *
     * @param accessDetailService 需要设置的查询授权服务的对象
     * @return 本对象实体，链式调用
     */
    public AuthFilter setAccessDetailService(AccessDetailService accessDetailService) {
        this.accessDetailService = accessDetailService;
        return this;
    }

    /**
     * 设置全局的配置对象，不调用此对象将会出现NullPointerException
     *
     * @param cryptoProperties 全局的配置文件
     * @return 本对象实体，链式调用
     */
    public AuthFilter setControlProperties(CompleteInterfaceCryptoProperties cryptoProperties) {
        this.cryptoProperties = cryptoProperties;
        return this;
    }

    /**
     * 设置过滤器的异常处理器，由于本对象
     *
     * @param handlerExceptionResolver 全局的配置文件
     * @return 本对象实体，链式调用
     */
    public AuthFilter setHandlerExceptionResolver(HandlerExceptionResolver handlerExceptionResolver) {
        this.handlerExceptionResolver = handlerExceptionResolver;
        return this;
    }
}
