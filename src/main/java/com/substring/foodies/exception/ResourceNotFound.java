package com.substring.foodies.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFound extends RuntimeException{

    public ResourceNotFound(String message)
    {
        super(message);
    }

    public ResourceNotFound()
    {
        super("Entity is not found");
    }
}
