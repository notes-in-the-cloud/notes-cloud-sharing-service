package com.notescloud.sharing_service.dto;

import com.notescloud.sharing_service.domain.NoteShareLink;

import java.time.LocalDateTime;
import java.util.UUID;

public record ShareLinkResponse(
    UUID id,
    UUID noteId,
    String url,
    LocalDateTime expiresAt
) {
    public static ShareLinkResponse from(NoteShareLink link, String baseUrl) {
        return new ShareLinkResponse(
            link.id(),
            link.noteId(),
            baseUrl + "/api/v1/share-links/" + link.token(),
            link.expiresAt()
        );
    }
}