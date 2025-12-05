package com.mysite.devtallks.common.identity.annotation;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {

    /**
     * required = true → 인증이 없으면 예외 발생
     * required = false → 인증 없으면 null 반환
     */
    boolean required() default true;
}
