package com.notescloud.sharing_service.dto;

import com.notescloud.sharing_service.domain.NoteShare;
import com.notescloud.sharing_service.domain.SharePermission;

import java.time.LocalDateTime;
import java.util.UUID;

public record NoteShareResponse(
    UUID id,
    UUID noteId,
    UUID ownerId,
    UUID sharedWithUserId,
    SharePermission permission,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static NoteShareResponse from(NoteShare share) {
        return new NoteShareResponse(
            share.id(),
            share.noteId(),
            share.ownerId(),
            share.sharedWithUserId(),
            share.permission(),
            share.createdAt(),
            share.updatedAt()
        );
    }
}