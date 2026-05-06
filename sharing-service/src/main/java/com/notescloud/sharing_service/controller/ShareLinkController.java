package com.notescloud.sharing_service.controller;

import com.notescloud.sharing_service.client.NotesServiceClient;
import com.notescloud.sharing_service.domain.NoteShareLink;
import com.notescloud.sharing_service.dto.ShareLinkResponse;
import com.notescloud.sharing_service.dto.SharedNoteResponse;
import com.notescloud.sharing_service.service.ShareLinkService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class ShareLinkController {
    private final NotesServiceClient notesServiceClient;
    private final ShareLinkService shareLinkService;

    public ShareLinkController(ShareLinkService shareLinkService,
                               NotesServiceClient notesServiceClient) {
        this.shareLinkService = shareLinkService;
        this.notesServiceClient = notesServiceClient;
    }

    @PostMapping("/users/{userId}/notes/{noteId}/share-links")
    @ResponseStatus(HttpStatus.CREATED)
    public ShareLinkResponse createShareLink(
        @PathVariable UUID userId,
        @PathVariable UUID noteId) {
        return shareLinkService.createShareLink(userId, noteId);
    }

    @GetMapping("/share-links/{token}")
    public SharedNoteResponse openShareLink(@PathVariable String token) {
        NoteShareLink link = shareLinkService.validateShareLink(token);

        return notesServiceClient.getNoteById(
            link.ownerId(),
            link.noteId());
    }
}
