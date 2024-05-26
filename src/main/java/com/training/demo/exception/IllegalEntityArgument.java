package com.training.demo.exception;

import com.training.demo.model.AnEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class IllegalEntityArgument extends EntityException {
    public IllegalEntityArgument(final Class<? extends AnEntity> clazz, final String illegalArgument, final String responseBody) {
        super(clazz, String.format("%s with %s not found", clazz.getSimpleName(), illegalArgument), responseBody);
    }

    @Override
    public HttpStatusCode getHttpStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }
}