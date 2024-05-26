package com.training.demo.exception;

import com.training.demo.model.AnEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class EntityNotFoundException extends EntityException {
    public EntityNotFoundException(final Class<? extends AnEntity> clazz, final Long id, final String responseBody) {
        super(clazz, String.format("%s with id=%d not found", clazz.getSimpleName(), id), responseBody);
    }

    @Override
    public HttpStatusCode getHttpStatusCode() {
        return HttpStatus.NOT_FOUND;
    }
}