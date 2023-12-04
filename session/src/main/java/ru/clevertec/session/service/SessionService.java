package ru.clevertec.session.service;

import ru.clevertec.session.dto.SessionRequest;
import ru.clevertec.session.dto.SessionResponse;

public interface SessionService {
    SessionResponse getOrCreateAndGetSession(SessionRequest request);
    void deleteSessions();
}
