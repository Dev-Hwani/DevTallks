package com.mysite.devtallks.common.identity.resolver;

import com.mysite.devtallks.common.identity.annotation.CurrentUser;
import com.mysite.devtallks.common.identity.exception.AuthenticationNotFoundException;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {

        CurrentUser annotation = parameter.getParameterAnnotation(CurrentUser.class);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증 정보가 없는 경우
        if (authentication == null ||
            !authentication.isAuthenticated() ||
            authentication.getPrincipal() == null ||
            "anonymousUser".equals(authentication.getPrincipal())) {

            if (annotation.required()) {
                throw new AuthenticationNotFoundException();
            }
            return null;    // required = false
        }

        // principal 그대로 반환 (UserDetails or CustomUser or username 등)
        return authentication.getPrincipal();
    }
}
