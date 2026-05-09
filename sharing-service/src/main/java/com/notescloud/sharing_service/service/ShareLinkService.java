package com.notescloud.sharing_service.service;

import com.notescloud.sharing_service.domain.NoteShareLink;
import com.notescloud.sharing_service.dto.ShareLinkResponse;
import com.notescloud.sharing_service.exception.ResourceNotFoundException;
import com.notescloud.sharing_service.exception.ShareLinkExpiredException;
import com.notescloud.sharing_service.repository.NoteShareLinkRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
public class ShareLinkService {
    private static final long SHARE_LINK_EXPIRATION_MINUTES = 5;
    private static final int TOKEN_BYTES = 32;

    private final NoteShareLinkRepository noteShareLinkRepository;
    private final SecureRandom secureRandom = new SecureRandom();
    private final String publicBaseUrl;

    public ShareLinkService(
        NoteShareLinkRepository noteShareLinkRepository,
        @Value("${sharing.public-base-url:http://localhost:8083}") String publicBaseUrl
    ) {
        this.noteShareLinkRepository = noteShareLinkRepository;
        this.publicBaseUrl = publicBaseUrl;
    }

    @Transactional
    public ShareLinkResponse createShareLink(UUID ownerId, UUID noteId) {
        String token = generateUniqueToken();

        LocalDateTime expiresAt = LocalDateTime.now()
            .plusMinutes(SHARE_LINK_EXPIRATION_MINUTES);

        NoteShareLink link = new NoteShareLink(
            noteId,
            ownerId,
            token,
            expiresAt
        );

        NoteShareLink savedLink = noteShareLinkRepository.save(link);

        return ShareLinkResponse.from(savedLink, publicBaseUrl);
    }

    @Transactional(readOnly = true)
    public NoteShareLink validateShareLink(String token) {
        NoteShareLink link = noteShareLinkRepository.findByToken(token)
            .orElseThrow(() -> new ResourceNotFoundException("Share link not found"));

        if (link.isExpired()) {
            throw new ShareLinkExpiredException("Share link has expired");
        }

        return link;
    }

    private String generateUniqueToken() {
        String token;

        do {
            token = generateToken();
        } while (noteShareLinkRepository.existsByToken(token));

        return token;
    }

    private String generateToken() {
        byte[] bytes = new byte[TOKEN_BYTES];
        secureRandom.nextBytes(bytes);

        return Base64.getUrlEncoder()
            .withoutPadding()
            .encodeToString(bytes);
    }
}