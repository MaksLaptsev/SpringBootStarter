package ru.clevertec.starter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import ru.clevertec.starter.annotation.SessionCheck;
import ru.clevertec.starter.exception.BlackListLogin;
import ru.clevertec.starter.exception.InvalidLoginValue;
import ru.clevertec.starter.exception.InvalidMethodParams;
import ru.clevertec.starter.model.Session;
import ru.clevertec.starter.properties.SessionCheckProperties;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SessionCheckInterceptor implements MethodInterceptor {
    private final Object originalBean;
    private final SessionCheckProperties sessionCheckProperties;
    private final BeanFactory beanFactory;
    private final SessionCheckService sessionCheckService;


    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if (method.isAnnotationPresent(SessionCheck.class)) {
            SessionCheck annotation = method.getAnnotation(SessionCheck.class);
            Set<String> finalBlackList = getFinalBlackList(annotation);

            if (paramsIsHasSessionObj(args)) {
                Object[] updateArgs = Optional.of(args)
                        .map(this::checkHaveParamsObjectsFieldLogin)
                        .map(this::checkAndGetLoginIfNotEmpty)
                        .map(login -> checkLoginInBlackList(login, finalBlackList))
                        .map(login -> getArgsWithUpdateSessionObject(args, login))
                        .get();
                return method.invoke(originalBean, updateArgs);
            } else
                throw new InvalidMethodParams("The session object is missing from the method: %s!".formatted(method.getName()));
        }
        return method.invoke(originalBean, args);
    }

    private Set<String> getFinalBlackList(SessionCheck annotation) {
        Set<String> finalBlackList = Arrays.stream(annotation.blackList()).collect(Collectors.toSet());
        if (annotation.includeDefaultBlackList()) {
            finalBlackList.addAll(sessionCheckProperties.getDefaultLoginBlackList().stream()
                    .map(beanFactory::getBean)
                    .map(SessionLogins::getBlackList)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet()));
            finalBlackList.addAll(sessionCheckProperties.getDefaultBlackList());
        }
        finalBlackList.addAll(Arrays.stream(annotation.defaultBlackList())
                .map(beanFactory::getBean)
                .map(SessionLogins::getBlackList)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet()));
        return finalBlackList;
    }

    private boolean paramsIsHasSessionObj(Object[] args) {
        return Arrays.stream(args).anyMatch(Session.class::isInstance);
    }

    private Object[] checkHaveParamsObjectsFieldLogin(Object[] args) {
        long objWithFieldLogin = Arrays.stream(args)
                .filter(object -> !(object instanceof Session))
                .filter(object -> Arrays.stream(object.getClass().getDeclaredFields())
                        .anyMatch(field -> field.getName().equals("login")))
                .count();
        if (objWithFieldLogin == 0 || objWithFieldLogin > 1) {
            throw new InvalidMethodParams("In method params more 1 or zero objects with field 'login'!");
        } else return args;
    }


    private String checkAndGetLoginIfNotEmpty(Object[] args) {
        return Arrays.stream(args)
                .filter(object -> !(object instanceof Session))
                .filter(object -> Arrays.stream(object.getClass().getDeclaredFields()).anyMatch(field -> field.getName().equals("login")))
                .map(this::getLoginValue)
                .filter(Objects::nonNull)
                .filter(x -> !x.isEmpty())
                .findFirst()
                .orElseThrow(() -> new InvalidLoginValue("Login is empty or null!"));
    }

    private String getLoginValue(Object o) {
        Method method;
        try {
            if (o.getClass().isRecord()) {
                method = o.getClass().getDeclaredMethod("login");
            } else {
                method = o.getClass().getDeclaredMethod("getLogin");
            }
        } catch (NoSuchMethodException e) {
            throw new InvalidLoginValue("Object: %s with field 'login' doesn't have getter!".formatted(o.getClass()));
        }
        method.setAccessible(true);
        try {
            String login = (String) method.invoke(o);
            return login;
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new InvalidLoginValue("");
        }
    }

    private String checkLoginInBlackList(String login, Set<String> blackList) {
        if (blackList.contains(login)) {
            throw new BlackListLogin("Login %s in black list".formatted(login));
        } else return login;
    }

    private Object[] getArgsWithUpdateSessionObject(Object[] args, String login) {
        return Arrays.stream(args)
                .map(object -> {
                    if (object instanceof Session) {
                        return sessionCheckService.findOrCreateAndGetByLogin(login);
                    } else return object;
                })
                .toArray();
    }
}
