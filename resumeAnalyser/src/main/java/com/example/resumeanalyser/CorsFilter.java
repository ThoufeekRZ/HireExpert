package com.example.resumeanalyser;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;

@WebFilter("/*") // Applies to all servlets
public class CorsFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {}

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletResponse res = (HttpServletResponse) response;
            HttpServletRequest req = (HttpServletRequest) request;

            System.out.println("vanthutan");

            res.setHeader("Access-Control-Allow-Origin", "http://localhost:5173"); // Allow requests from React
            //res.setHeader("Access-Control-Allow-Origin", "http://172.17.20.178:5174");
            res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            res.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
            res.setHeader("Access-Control-Allow-Credentials", "true");

            // Stop processing for preflight (OPTIONS) requests
            if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
                res.setStatus(HttpServletResponse.SC_OK);
                return;
            }

            chain.doFilter(request, response);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroy() {}
}
