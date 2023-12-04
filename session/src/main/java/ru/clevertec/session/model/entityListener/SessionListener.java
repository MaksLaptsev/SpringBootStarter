package ru.clevertec.session.model.entityListener;

import jakarta.persistence.PrePersist;
import ru.clevertec.session.model.Session;
import java.time.LocalDateTime;

public class SessionListener {
    @PrePersist
    public void beforeSaveAddCreatedDate(Session session){
        session.setSessionOpenDate(LocalDateTime.now());
    }
}
