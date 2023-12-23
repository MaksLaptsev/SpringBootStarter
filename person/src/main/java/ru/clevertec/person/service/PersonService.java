package ru.clevertec.person.service;

import ru.clevertec.person.dto.PersonRequest;
import ru.clevertec.person.dto.PersonResponse;
import ru.clevertec.starter.model.Session;

public interface PersonService {
    PersonResponse getById(long id, Session session);

    PersonResponse getByLogin(PersonRequest request, Session session);

    PersonResponse save(PersonRequest request, Session session);
}
