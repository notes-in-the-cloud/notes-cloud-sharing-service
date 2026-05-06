package com.notescloud.sharing_service.controller;

import com.notescloud.sharing_service.dto.CreateNoteShareRequest;
import com.notescloud.sharing_service.dto.NoteShareResponse;
import com.notescloud.sharing_service.dto.UpdateSharePermissionRequest;
import com.notescloud.sharing_service.service.NoteShareService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/shares")
public class NoteShareController {

    private final NoteShareService noteShareService;

    public NoteShareController(NoteShareService noteShareService) {
        this.noteShareService = noteShareService;
    }

    @PostMapping
    public NoteShareResponse shareNote(@Valid @RequestBody CreateNoteShareRequest request) {
        return noteShareService.shareNote(request);
    }

    @GetMapping("/note/{noteId}")
    public List<NoteShareResponse> getSharesForNote(@PathVariable UUID noteId) {
        return noteShareService.getSharesForNote(noteId);
    }

    @GetMapping("/user/{userId}")
    public List<NoteShareResponse> getSharesForUser(@PathVariable UUID userId) {
        return noteShareService.getSharesForUser(userId);
    }

    @GetMapping("/check")
    public boolean hasAccess(@RequestParam UUID noteId, @RequestParam UUID userId) {
        return noteShareService.hasAccess(noteId, userId);
    }

    @PutMapping("/{shareId}/permission")
    public NoteShareResponse updatePermission(
        @PathVariable UUID shareId,
        @Valid @RequestBody UpdateSharePermissionRequest request
    ) {
        return noteShareService.updatePermission(shareId, request);
    }

    @DeleteMapping("/{shareId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteShare(@PathVariable UUID shareId) {
        noteShareService.deleteShare(shareId);
    }
}