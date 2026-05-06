package com.notescloud.sharing_service.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record SharedNoteResponse(
    UUID id,
    UUID userId,
    String title,
    String content,
    String color,
    LocalDateTime updatedAt,
    LocalDateTime createdAt
) { }
