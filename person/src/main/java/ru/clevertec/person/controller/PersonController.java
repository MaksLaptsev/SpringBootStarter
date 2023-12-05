package ru.clevertec.person.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.person.dto.PersonRequest;
import ru.clevertec.person.dto.PersonResponse;
import ru.clevertec.person.model.Person;
import ru.clevertec.person.service.PersonService;
import ru.clevertec.starter.annotation.SessionCheck;
import ru.clevertec.starter.model.Session;

@RestController
@RequestMapping("/person")
public class PersonController {
    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @SessionCheck
    @GetMapping({"{id}"})
    public ResponseEntity<PersonResponse> get(@PathVariable Long id, @RequestBody Person person, Session session){
        return ResponseEntity.status(HttpStatus.OK).body(personService.getById(id,session));
    }

    @SessionCheck(blackList = {"Black","White"})
    @PostMapping
    public ResponseEntity<PersonResponse> save(@RequestBody PersonRequest request, Session session){
        return ResponseEntity.status(HttpStatus.CREATED).body(personService.save(request,session));
    }

    @SessionCheck(blackList = {"Maks"})
    @GetMapping("/byLogin")
    public ResponseEntity<PersonResponse> getByLogin(@RequestBody PersonRequest request, Session session){
        return ResponseEntity.status(HttpStatus.CREATED).body(personService.getByLogin(request,session));
    }
}
