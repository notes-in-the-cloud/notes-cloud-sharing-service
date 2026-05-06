package com.notescloud.sharing_service.exception;

public class ShareLinkExpiredException extends RuntimeException {
    public ShareLinkExpiredException(String message) {
        super(message);
    }
}
