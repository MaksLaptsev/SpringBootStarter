package ru.clevertec.person.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.person.dto.PersonRequest;
import ru.clevertec.person.dto.PersonResponse;
import ru.clevertec.person.exception.PersonNotFoundException;
import ru.clevertec.person.mapper.PersonMapper;
import ru.clevertec.person.model.Person;
import ru.clevertec.person.repository.PersonRepository;
import ru.clevertec.person.service.PersonService;
import ru.clevertec.starter.model.Session;

import java.util.Optional;

@Service
@Transactional
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    @Override
    public PersonResponse getById(long id, Session session) {
        return personRepository.findById(id)
                .map(p -> personMapper.toResponse(p, session))
                .orElseThrow(() -> new PersonNotFoundException("Person with id: %s not found".formatted(id)));
    }

    @Override
    public PersonResponse getByLogin(PersonRequest request, Session session) {
        return personRepository.findByLogin(personMapper.fromRequest(request).getLogin())
                .map(p -> personMapper.toResponse(p, session))
                .orElseThrow(() -> new PersonNotFoundException("Person with login: %s not found"
                        .formatted(personMapper.fromRequest(request).getLogin())));
    }

    @Override
    public PersonResponse save(PersonRequest request, Session session) {
        Person person = personMapper.fromRequest(request);
        return Optional.of(personRepository.saveAndFlush(person))
                .map(p -> personMapper.toResponse(p, session))
                .orElseThrow();
    }
}
