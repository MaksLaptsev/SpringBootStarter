package ru.clevertec.starter.annotation;

import ru.clevertec.starter.service.SessionLogins;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SessionCheck {
    String[] blackList() default {};

    boolean includeDefaultBlackList() default true;

    Class<? extends SessionLogins>[] defaultBlackList() default {};
}
