package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否登录
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

    //路径匹配器
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {


        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1，获取本次请求uri
        String requestURI = request.getRequestURI();

        log.info("拦截到请求：{}", requestURI);

        //无需处理
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };

        //2，判断是否需要处理
        boolean check = check(urls, requestURI);

        //3，不需要处理
        if (check) {
            log.info("请求无需处理：{}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        //4-1，已经登录，可以放行
        if (request.getSession().getAttribute("employee") != null) {
            log.info("用户已经登陆，id为：{}", request.getSession().getAttribute("employee"));

            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);


            Long id = Thread.currentThread().getId();
            log.info("线程id为:{}", id);

            filterChain.doFilter(request, response);
            return;
        }

        //4-2，已经登录，可以放行
        if (request.getSession().getAttribute("user") != null) {
            log.info("用户已经登陆，id为：{}", request.getSession().getAttribute("user"));

            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);


            Long id = Thread.currentThread().getId();
            log.info("线程id为:{}", id);

            filterChain.doFilter(request, response);
            return;
        }

        //5，未登录
        log.info("未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }


    /**
     * 检查本次是否需要放行
     *
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }

}
