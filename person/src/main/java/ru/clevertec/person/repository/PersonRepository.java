package ru.clevertec.person.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.person.model.Person;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByLogin(String login);
}
