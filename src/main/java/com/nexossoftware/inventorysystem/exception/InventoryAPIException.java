package com.nexossoftware.inventorysystem.exception;


import org.springframework.http.HttpStatus;

public class InventoryAPIException extends RuntimeException {

    private final HttpStatus status;
    private final String message;

    public InventoryAPIException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
