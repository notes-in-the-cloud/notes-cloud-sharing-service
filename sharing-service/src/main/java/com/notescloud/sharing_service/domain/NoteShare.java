package com.notescloud.sharing_service.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
    schema = "sharing",
    name = "note_shares",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_note_shared_user",
            columnNames = {"note_id", "shared_with_user_id"}
        )
    }
)
public class NoteShare {

    @Id
    private UUID id;

    @Column(name = "note_id", nullable = false)
    private UUID noteId;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @Column(name = "shared_with_user_id", nullable = false)
    private UUID sharedWithUserId;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission", nullable = false)
    private SharePermission permission;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected NoteShare() {
    }

    public NoteShare(UUID noteId, UUID ownerId, UUID sharedWithUserId, SharePermission permission) {
        this.id = UUID.randomUUID();
        this.noteId = noteId;
        this.ownerId = ownerId;
        this.sharedWithUserId = sharedWithUserId;
        this.permission = permission;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updatePermission(SharePermission permission) {
        this.permission = permission;
        this.updatedAt = LocalDateTime.now();
    }

    public UUID id() {
        return id;
    }

    public UUID noteId() {
        return noteId;
    }

    public UUID ownerId() {
        return ownerId;
    }

    public UUID sharedWithUserId() {
        return sharedWithUserId;
    }

    public SharePermission permission() {
        return permission;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    public LocalDateTime updatedAt() {
        return updatedAt;
    }
}