package com.notescloud.sharing_service.service;

import com.notescloud.sharing_service.domain.NoteShare;
import com.notescloud.sharing_service.dto.CreateNoteShareRequest;
import com.notescloud.sharing_service.dto.NoteShareResponse;
import com.notescloud.sharing_service.dto.UpdateSharePermissionRequest;
import com.notescloud.sharing_service.exception.ResourceNotFoundException;
import com.notescloud.sharing_service.repository.NoteShareRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NoteShareService {

    private final NoteShareRepository noteShareRepository;

    public NoteShareService(NoteShareRepository noteShareRepository) {
        this.noteShareRepository = noteShareRepository;
    }

    public NoteShareResponse shareNote(CreateNoteShareRequest request) {
        if (noteShareRepository.existsByNoteIdAndSharedWithUserId(
            request.noteId(),
            request.sharedWithUserId()
        )) {
            throw new IllegalArgumentException("Note is already shared with this user.");
        }

        NoteShare share = new NoteShare(
            request.noteId(),
            request.ownerId(),
            request.sharedWithUserId(),
            request.permission()
        );

        return NoteShareResponse.from(noteShareRepository.save(share));
    }

    public List<NoteShareResponse> getSharesForNote(UUID noteId) {
        return noteShareRepository.findAllByNoteId(noteId)
            .stream()
            .map(NoteShareResponse::from)
            .toList();
    }

    public List<NoteShareResponse> getSharesForUser(UUID userId) {
        return noteShareRepository.findAllBySharedWithUserId(userId)
            .stream()
            .map(NoteShareResponse::from)
            .toList();
    }

    public NoteShareResponse updatePermission(UUID shareId, UpdateSharePermissionRequest request) {
        NoteShare share = noteShareRepository.findById(shareId)
            .orElseThrow(() -> new ResourceNotFoundException("Share not found with id: " + shareId));

        share.updatePermission(request.permission());

        return NoteShareResponse.from(noteShareRepository.save(share));
    }

    public void deleteShare(UUID shareId) {
        if (!noteShareRepository.existsById(shareId)) {
            throw new ResourceNotFoundException("Share not found with id: " + shareId);
        }

        noteShareRepository.deleteById(shareId);
    }

    public boolean hasAccess(UUID noteId, UUID userId) {
        return noteShareRepository.existsByNoteIdAndSharedWithUserId(noteId, userId);
    }
}