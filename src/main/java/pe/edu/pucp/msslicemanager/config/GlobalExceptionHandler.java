package pe.edu.pucp.msslicemanager.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pe.edu.pucp.msslicemanager.exception.SliceActiveException;
import pe.edu.pucp.msslicemanager.exception.SliceNotFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SliceActiveException.class)
    public ResponseEntity<ErrorResponse> handlerCustomerErrors2(SliceActiveException e){
        List<String> errors = Collections.singletonList(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(), errors);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SliceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerCustomerErrors(SliceNotFoundException e){
        List<String> errors = Collections.singletonList(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.getReasonPhrase(), errors);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(ConstraintViolationException e){
        List<String> errors = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(), errors);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(HttpRequestMethodNotSupportedException e){
        List<String> errors = new ArrayList<>();
        e.getSupportedHttpMethods().forEach(method -> {
            String error = "Method not supported: " + method;
            errors.add(error);
        });

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(), errors);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED);
    }
    private static class ErrorResponse {
        private String status;
        private List<String> errors;

        public ErrorResponse(String status, List<String> errors) {
            this.status = status;
            this.errors = errors;
        }

        public String getStatus() {
            return status;
        }

        public List<String> getErrors() {
            return errors;
        }
    }
}
