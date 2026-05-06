package com.notescloud.sharing_service.dto;

import com.notescloud.sharing_service.domain.SharePermission;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateNoteShareRequest(
    @NotNull UUID noteId,
    @NotNull UUID ownerId,
    @NotNull UUID sharedWithUserId,
    @NotNull SharePermission permission
) {
}