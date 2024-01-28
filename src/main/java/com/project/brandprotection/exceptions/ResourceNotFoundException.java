package com.project.brandprotection.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " could not be found for id: " + id);
    }

    public ResourceNotFoundException(String msg) {
        super(msg);
    }


}