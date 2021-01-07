package com.hussam.inventory.inventory.exception;

import java.util.Date;
import java.util.List;

public class ExceptionResponse {

    private Date timestamp;
    private String message;
    private String path;

    public ExceptionResponse(Date timestamp, String message,String path) {
        this.timestamp = timestamp;
        this.message = message;
        this.path = path;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return path;
    }
}
