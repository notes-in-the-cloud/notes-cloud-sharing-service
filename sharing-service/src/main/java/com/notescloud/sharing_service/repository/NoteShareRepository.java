package com.notescloud.sharing_service.repository;

import com.notescloud.sharing_service.domain.NoteShare;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NoteShareRepository extends JpaRepository<NoteShare, UUID> {
    List<NoteShare> findAllByNoteId(UUID noteId);

    List<NoteShare> findAllBySharedWithUserId(UUID sharedWithUserId);

    Optional<NoteShare> findByNoteIdAndSharedWithUserId(UUID noteId, UUID sharedWithUserId);

    boolean existsByNoteIdAndSharedWithUserId(UUID noteId, UUID sharedWithUserId);
}