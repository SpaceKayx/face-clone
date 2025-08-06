package com.core.utils;

import com.core.constants.FConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

public class HttpServletRequestUtil {

    /**
     * Lấy userId từ HttpServletRequest truyền vào
     */
    public static String getUserIdFromHeader(HttpServletRequest request) {
        if (request == null) return null;
        return request.getHeader(FConstants.USER_ID);
    }

    /**
     * Lấy userId hiện tại từ RequestContextHolder (không cần truyền request)
     */
    public static UUID getCurrentUserId() {
        HttpServletRequest request = getCurrentHttpRequest();
        if (request == null) return null;
        return UUID.fromString(request.getHeader(FConstants.USER_ID));
    }

    /**
     * Lấy HttpServletRequest hiện tại từ Spring context
     */
    public static HttpServletRequest getCurrentHttpRequest() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) attributes).getRequest();
        }
        return null;
    }

    /**
     * Lấy userId hiện tại từ RequestContextHolder
     */
    public static String getCurrentUserForBaseEntity() {
        HttpServletRequest request = getCurrentHttpRequest();
        if (request == null) return FConstants.BASE_USER;

        return request.getHeader(FConstants.USER_ID);
    }

}
