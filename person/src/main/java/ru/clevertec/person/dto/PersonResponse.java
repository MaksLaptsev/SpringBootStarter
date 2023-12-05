package ru.clevertec.person.dto;

import ru.clevertec.starter.model.Session;

public record PersonResponse(Long id,
                             String name,
                             String login,
                             Session session) {
}
