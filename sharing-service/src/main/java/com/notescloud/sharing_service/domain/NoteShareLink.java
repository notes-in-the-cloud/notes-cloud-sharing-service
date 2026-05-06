package com.notescloud.sharing_service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "note_share_links", schema = "sharing")
public class NoteShareLink {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID noteId;

    @Column(nullable = false)
    private UUID ownerId;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected NoteShareLink() {
    }

    public NoteShareLink(UUID noteId, UUID ownerId, String token, LocalDateTime expiresAt) {
        this.noteId = noteId;
        this.ownerId = ownerId;
        this.token = token;
        this.expiresAt = expiresAt;
        this.createdAt = LocalDateTime.now();
    }

    public boolean isExpired() {
        return expiresAt.isBefore(LocalDateTime.now());
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

    public String token() {
        return token;
    }

    public LocalDateTime expiresAt() {
        return expiresAt;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }
}