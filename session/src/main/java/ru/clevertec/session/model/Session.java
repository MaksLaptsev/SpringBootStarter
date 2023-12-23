package ru.clevertec.session.model;

import jakarta.persistence.*;
import lombok.Data;
import ru.clevertec.session.model.entityListener.SessionListener;

import java.time.LocalDateTime;

@Data
@Entity
@EntityListeners(SessionListener.class)
@Table(name = "session")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "login")
    private String login;
    @Column(name = "sessionopendate")
    private LocalDateTime sessionOpenDate;
}
