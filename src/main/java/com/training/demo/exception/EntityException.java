package com.training.demo.exception;

import com.training.demo.model.AnEntity;
import com.training.demo.util.ApiUtil;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@Getter
public abstract class EntityException extends IllegalArgumentException {
    /**
     * The class of entity concerned with the raised exception.
     */
    private final Class<? extends AnEntity> clazz;

    /**
     * The body that should be returned when this exception is raised.
     */
    private final String responseBody;

    abstract public HttpStatusCode getHttpStatusCode();

    EntityException(final Class<? extends AnEntity> clazz, final String message, final String responseBody) {
        super(message);
        this.clazz = clazz;
        this.responseBody = responseBody;
    }

    public ResponseEntity<String> getResponseEntity() {
        HttpHeaders httpHeaders = new HttpHeaders();
        ApiUtil.putExceptionInResponseHeaders(httpHeaders, this);
        return new ResponseEntity<>(
                responseBody,
                httpHeaders,
                getHttpStatusCode()
        );
    }
}