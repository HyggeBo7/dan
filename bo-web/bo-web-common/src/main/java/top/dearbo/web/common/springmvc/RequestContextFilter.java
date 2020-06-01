package top.dearbo.web.common.springmvc;

import top.dearbo.util.data.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Bo
 */
public class RequestContextFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestContextFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("request context filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        long start = System.currentTimeMillis();
        RequestContext.init((HttpServletRequest) request, (HttpServletResponse) response, null);

        logger.info("request start| seq:{}, uri:{}, param:{}, cookie:{} ",
                RequestContext.getSeq(), ((HttpServletRequest) request).getRequestURI(),
                ((HttpServletRequest) request).getQueryString(),
                JsonUtil.toJson(((HttpServletRequest) request).getCookies()));
        chain.doFilter(request, response);

        logger.info("request end| seq:{}, spend time:{}, response:{}",
                RequestContext.getSeq(), (System.currentTimeMillis() - start), response.getContentType());

        RequestContext.clear();

    }

    @Override
    public void destroy() {
        logger.info("request context filter destroy");
    }
}
