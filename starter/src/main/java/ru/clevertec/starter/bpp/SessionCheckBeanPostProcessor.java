package ru.clevertec.starter.bpp;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import ru.clevertec.starter.annotation.SessionCheck;
import ru.clevertec.starter.properties.SessionCheckProperties;
import ru.clevertec.starter.service.SessionCheckInterceptor;
import ru.clevertec.starter.service.SessionCheckService;

import java.lang.reflect.Constructor;
import java.util.*;

public class SessionCheckBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {
    private Map<String, Class<?>> beanNamesWithAnnotatedMethods = new HashMap<>();
    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        boolean beanHasAnnotation = Arrays.stream(clazz.getMethods())
                .anyMatch(method -> method.isAnnotationPresent(SessionCheck.class));
        if (beanHasAnnotation) {
            beanNamesWithAnnotatedMethods.put(beanName, clazz);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return Optional.ofNullable(beanNamesWithAnnotatedMethods.get(beanName))
                .map(clazz -> getSessionCheckProxy(bean))
                .orElse(bean);
    }

    private Object getSessionCheckProxy(Object bean) {
        SessionCheckService sessionCheckService = beanFactory.getBean(SessionCheckService.class);
        SessionCheckProperties sessionCheckProperties = beanFactory.getBean(SessionCheckProperties.class);
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(bean.getClass());
        enhancer.setCallback(new SessionCheckInterceptor(bean, sessionCheckProperties, beanFactory, sessionCheckService));
        return isPresentDefaultConstructor(bean)
                ? enhancer.create()
                : enhancer.create(getNotDefaultConstructorArgTypes(bean), getNotDefaultConstructorArgs(bean));
    }

    private boolean isPresentDefaultConstructor(Object bean) {
        return Arrays.stream(bean.getClass().getConstructors())
                .anyMatch(constructor -> constructor.getParameterCount() == 0);
    }

    private Class<?>[] getNotDefaultConstructorArgTypes(Object object) {
        return Arrays.stream(object.getClass().getConstructors())
                .max(Comparator.comparingInt(Constructor::getParameterCount))
                .map(Constructor::getParameterTypes)
                .orElseThrow(IllegalArgumentException::new);
    }

    private Object[] getNotDefaultConstructorArgs(Object object) {
        Class<?>[] constructorArgTypes = getNotDefaultConstructorArgTypes(object);
        return Arrays.stream(constructorArgTypes)
                .map(beanFactory::getBean)
                .toArray();
    }


}
