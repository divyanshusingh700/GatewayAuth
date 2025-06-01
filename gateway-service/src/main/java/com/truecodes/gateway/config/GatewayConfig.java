package com.truecodes.gateway.config;

import com.truecodes.gateway.filter.JwtAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> registerJwtFilter(JwtAuthenticationFilter filter) {
        FilterRegistrationBean<JwtAuthenticationFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(filter);
        registration.addUrlPatterns("/user/*"); // or "/*" if you want it globally
        registration.setOrder(1);
        return registration;
    }

}
