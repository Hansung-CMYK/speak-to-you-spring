package com.cmyk.ego.speaktoyouspring.config.multitenancy;

/// 현재 스레드의 스키마 정보를 담아두는 클래스이다.
public class TenantContext {
    /// tenant Database의 기본 스키마 이름
    public static String DEFAULT_SCHEMA = "public";

    /// 현재 테넌트의 정보를 담아두기 위한 객체
    private static ThreadLocal<String> currentTenant = new InheritableThreadLocal<>();

    /// 현재 테넌트의 이름(스키마 명)을 기록하는 함수
    public static void setCurrentTenant(String tenantName) {
        currentTenant.set(tenantName);
    }

    /// 현재 테넌트의 이름(스키마 명)을 반환하는 함수
    public static String getCurrentTenant() {
        return currentTenant.get();
    }

    /// 현재 테넌트의 정보를 제거하는 함수이다.
    ///
    /// 모든 작업이 끝나면 테넌트 정보를 제거한다.
    public static void clear() {
        currentTenant.remove();
    }
}
