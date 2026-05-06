package com.notescloud.sharing_service.dto;

import com.notescloud.sharing_service.domain.SharePermission;
import jakarta.validation.constraints.NotNull;

public record UpdateSharePermissionRequest(
    @NotNull SharePermission permission
) {
}