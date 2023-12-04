package ru.clevertec.session.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.session.model.Session;
import java.time.LocalDateTime;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session,Long> {
    Optional<Session> findByLogin(String login);
    void deleteAllBySessionOpenDateBefore(LocalDateTime localDateTime);
}
