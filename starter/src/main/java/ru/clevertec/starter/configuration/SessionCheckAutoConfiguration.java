package ru.clevertec.starter.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
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
    public WebClient webClient(){
        return WebClient.builder().build();
    }

    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        return objectMapper;
    }

    @Bean
    public SessionCheckService sessionCheckService(WebClient webClient,SessionCheckProperties properties,ObjectMapper objectMapper){
        return new SessionCheckService(webClient, properties.getUrl(),objectMapper);
    }
}
