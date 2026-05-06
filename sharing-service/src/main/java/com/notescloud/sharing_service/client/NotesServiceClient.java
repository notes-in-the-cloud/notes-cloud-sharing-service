package com.notescloud.sharing_service.client;

import com.notescloud.sharing_service.dto.SharedNoteResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
public class NotesServiceClient {

    private final RestClient restClient;

    public NotesServiceClient(
        @Value("${notes-service.url:http://localhost:8082}") String notesServiceUrl
    ) {
        this.restClient = RestClient.builder()
            .baseUrl(notesServiceUrl)
            .build();
    }

    public SharedNoteResponse getNoteById(UUID userId, UUID noteId) {
        return restClient.get()
            .uri("/api/users/{userId}/notes/{noteId}", userId, noteId)
            .retrieve()
            .body(SharedNoteResponse.class);
    }
}