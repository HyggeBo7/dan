package top.dearbo.web.springmvc.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @fileName: OriginInterceptor
 * @author: Dan
 * @createDate: 2019-03-26 13:22.
 * @description: 解决跨域问题
 */
public class RequestOriginInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RequestOriginInterceptor.class);

    private static final String DEFAULT_ORIGIN = "*";

    private String origin;

    public RequestOriginInterceptor() {
        this.origin = DEFAULT_ORIGIN;
    }

    public RequestOriginInterceptor(String origin) {
        this.origin = origin;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        response.setHeader("Access-control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
        logger.info("uri={},request-Origin=【{}】,response-Origin=【{}】", request.getRequestURI(), request.getHeader("Origin"), response.getHeader("Access-control-Allow-Origin"));
        return true;
    }

}
