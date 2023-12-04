package ru.clevertec.starter.service;

import java.util.Set;

public class SessionBlackListDefault implements SessionLogins{
    @Override
    public Set<String> getBlackList() {
        return Set.of("Veles","login","black");
    }
}
