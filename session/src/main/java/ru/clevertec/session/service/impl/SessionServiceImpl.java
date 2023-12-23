package ru.clevertec.session.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.session.dto.SessionRequest;
import ru.clevertec.session.dto.SessionResponse;
import ru.clevertec.session.mapper.SessionMapper;
import ru.clevertec.session.repository.SessionRepository;
import ru.clevertec.session.service.SessionService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class SessionServiceImpl implements SessionService {
    private final SessionRepository sessionRepository;
    private final SessionMapper sessionMapper;

    @Autowired
    public SessionServiceImpl(SessionRepository sessionRepository, SessionMapper sessionMapper) {
        this.sessionRepository = sessionRepository;
        this.sessionMapper = sessionMapper;
    }

    @Override
    public SessionResponse getOrCreateAndGetSession(SessionRequest request) {
        return sessionRepository.findByLogin(sessionMapper.fromRequest(request).getLogin())
                .map(sessionMapper::toResponse)
                .orElseGet(() -> Optional.of(sessionRepository.saveAndFlush(sessionMapper.fromRequest(request)))
                        .map(sessionMapper::toResponse)
                        .orElseThrow());
    }

    @Override
    @Scheduled(cron = "@daily")
    public void deleteSessions() {
        sessionRepository.deleteAllBySessionOpenDateBefore(LocalDateTime.now());
    }
}
