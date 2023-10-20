package com.fairgoods.webshop.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import jakarta.servlet.*;

import java.io.IOException;

/**
 * Filter to configure web settings, especially concerning CORS (Cross-Origin Resource Sharing).
 *
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebConfig implements Filter {
    /**
     * Initializes the filter. Overrides the default method in the Filter interface.
     *
     * @param filterConfig configuration for the filter.
     * @throws ServletException in case of errors.
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    /**
     * Performs the filtering task: setting headers for CORS and handling OPTIONS HTTP method.
     *
     * @param servletRequest  incoming request.
     * @param servletResponse outgoing response.
     * @param filterChain     remaining filters to apply.
     * @throws IOException      in case of I/O errors.
     * @throws ServletException in case of servlet errors.
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "*");
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    /**
     * Cleans up any resources used. Overrides the default method in the Filter interface.
     */
    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}