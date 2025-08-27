package me.sreejithnair.linkup.connection_service.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

import static me.sreejithnair.linkup.connection_service.auth.UserContextHolder.getCurrentUserId;

@Component
public class FeignClientInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        Long userId = getCurrentUserId();
        if (userId != null) {
            requestTemplate.header("X-User-Id", userId.toString());
        }
    }
}
