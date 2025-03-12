package com.cmyk.ego.speaktoyouspring.config.multitenancy;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

/// REST API 작업이 모두 끝난 후, 실행되는 함수를 정의한 클래스
@Component
public class TenantCleanupFilter extends GenericFilterBean {

    /// 작업이 끝난 후, TenantContext의 정보를 초기화 한다.
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } finally {
            // 요청이 끝날 때마다 ThreadLocal 초기화
            TenantContext.clear();
        }
    }
}
