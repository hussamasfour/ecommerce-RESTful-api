package com.hussam.inventory.inventory.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllException(Exception ex, WebRequest request){
        String details = ex.getLocalizedMessage();
        ExceptionResponse exceptionResponse=  new ExceptionResponse(new Date(), details, request.getDescription(false));
        return new ResponseEntity(exceptionResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException ex, WebRequest request){
        String details = ex.getLocalizedMessage();
        ExceptionResponse exceptionResponse=  new ExceptionResponse(new Date(), details, request.getDescription(false) );
        return new ResponseEntity(exceptionResponse,new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<ExceptionResponse> handleConstraintViolationExceptions(
            DataIntegrityViolationException ex) {

        String details = ex.getLocalizedMessage();
        ExceptionResponse exceptionResponse=  new ExceptionResponse(new Date(), "Validation Failed", details);
        return new ResponseEntity(exceptionResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidArgumentException.class)
    public final ResponseEntity<ExceptionResponse> handleInvalidArgumentExceptions(
            InvalidArgumentException ex) {

        String details = ex.getLocalizedMessage();
        ExceptionResponse exceptionResponse=  new ExceptionResponse(new Date(), "Validation Failed", details);
        return new ResponseEntity(exceptionResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

}
