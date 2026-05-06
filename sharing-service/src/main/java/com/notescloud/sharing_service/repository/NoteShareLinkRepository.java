package com.notescloud.sharing_service.repository;

import com.notescloud.sharing_service.domain.NoteShareLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface NoteShareLinkRepository extends JpaRepository<NoteShareLink, UUID> {
    Optional<NoteShareLink> findByToken(String token);

    boolean existsByToken(String token);
}