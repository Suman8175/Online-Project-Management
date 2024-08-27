package com.suman.projectManager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ProjectManagerException {

    @ExceptionHandler
    public ResponseEntity<ProjectCustomMessage> handleException(AlreadyExistsException exc) {

        ProjectCustomMessage error = new ProjectCustomMessage();
        error.setStatus(HttpStatus.CONFLICT.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
    @ExceptionHandler
    public ResponseEntity<ProjectCustomMessage> handleException(BadRequest exc) {
        ProjectCustomMessage error = new ProjectCustomMessage();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ProjectCustomMessage> handleException(EntityNotFoundException exc) {
        ProjectCustomMessage error = new ProjectCustomMessage();
        error.setStatus(HttpStatus.NO_CONTENT.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.NO_CONTENT);
    }


}
