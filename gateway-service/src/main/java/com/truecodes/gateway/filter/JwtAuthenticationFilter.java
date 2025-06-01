package com.truecodes.gateway.filter;

import com.truecodes.utilities.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter implements Filter {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Cast ServletRequest and ServletResponse to HTTP-specific versions
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        res.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");

        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            res.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        String path = req.getRequestURI();

        // Allow unauthenticated access to /auth path (optional, adjust as needed)
        if (path.startsWith("/auth")) {
            chain.doFilter(request, response);
            return;
        }

        if (path.startsWith("/user")) {
            String authHeader = req.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                String username = jwtUtil.extractUsername(token);

                if (username != null && jwtUtil.validateToken(token, username)) {

                    // Wrap request to add username header for downstream services
                    HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(req) {
                        @Override
                        public String getHeader(String name) {
                            if ("X-User-Email".equals(name)) return username;
                            return super.getHeader(name);
                        }
                    };

                    chain.doFilter(requestWrapper, response);
                    return;
                } else {
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    res.getWriter().write("Unauthorized: Invalid or expired token");
                    return;
                }
            } else {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.getWriter().write("Unauthorized: Missing or invalid Authorization header");
                return;
            }

        }

        // For other requests, continue the filter chain without checks
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
