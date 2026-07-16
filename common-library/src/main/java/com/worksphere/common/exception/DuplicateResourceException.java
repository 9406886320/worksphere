package com.worksphere.common.exception;

public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(
            String resource,
            String fieldName,
            Object fieldValue) {

        super(String.format(
                "%s already exists with %s : '%s'",
                resource,
                fieldName,
                fieldValue
        ));
    }
}