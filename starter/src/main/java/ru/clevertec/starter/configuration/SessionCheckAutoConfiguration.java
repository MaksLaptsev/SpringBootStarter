package ru.clevertec.starter.configuration;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;
import org.springframework.web.client.RestTemplate;
import ru.clevertec.starter.bpp.SessionCheckBeanPostProcessor;
import ru.clevertec.starter.properties.SessionCheckProperties;
import ru.clevertec.starter.service.SessionBlackListDefault;
import ru.clevertec.starter.service.SessionCheckService;
import ru.clevertec.starter.service.SessionLogins;

@AutoConfiguration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@EnableConfigurationProperties(SessionCheckProperties.class)
@ConditionalOnClass(SessionCheckProperties.class)
@ConditionalOnProperty(value = "session.check.enable", havingValue = "true")
public class SessionCheckAutoConfiguration {

    @Bean
    public SessionCheckBeanPostProcessor sessionCheckBeanPostProcessor(){
        return new SessionCheckBeanPostProcessor();
    }

    @Bean
    public SessionLogins sessionLogins(){
        return new SessionBlackListDefault();
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public SessionCheckService sessionCheckService(SessionCheckProperties properties,RestTemplate restTemplate){
        return new SessionCheckService(properties.getUrl(),restTemplate);
    }
}
