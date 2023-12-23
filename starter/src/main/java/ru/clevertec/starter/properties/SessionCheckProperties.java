package ru.clevertec.starter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import ru.clevertec.starter.service.SessionLogins;

import java.util.HashSet;
import java.util.Set;

@Data
@ConfigurationProperties(prefix = "session.check.default")
public class SessionCheckProperties {
    private Set<String> defaultBlackList = new HashSet<>();
    private Set<Class<? extends SessionLogins>> defaultLoginBlackList = new HashSet<>();
    private String url;
}
