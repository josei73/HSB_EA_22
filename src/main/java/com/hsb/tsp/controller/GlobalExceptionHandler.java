
package com.hsb.tsp.controller;



import com.hsb.tsp.exception.ErrorMessage;
import com.hsb.tsp.exception.HeldKarpException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;

/**
 * This class is for error handling and helps to response an object with a message
 * @ExceptionHandler configures the advice to only respond if an EmployeeNotFoundException is thrown.
 * @ResponseStatus says to issue an HttpStatus.NOT_FOUND, i.e. an HTTP 404.
 *
 * The body of the advice generates the content. In this case, it gives the message of the exception.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private String message;
    private Object object;


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }


    @ExceptionHandler({HeldKarpException.class})
    public final ResponseEntity<ErrorMessage> handleException(Exception ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();

        if (ex instanceof HeldKarpException) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            HeldKarpException unfe = (HeldKarpException) ex;

            return handleHeldException(unfe, headers, status, request);



        }
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return handleExceptionInternal(ex, null, headers, status, request);
    }


    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    private ResponseEntity<ErrorMessage> handleHeldException(HeldKarpException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorMessage errorMessages = new ErrorMessage(ex.getMessage(),HttpStatus.NOT_FOUND.toString());
        return handleExceptionInternal(ex, errorMessages, headers, status, request);
    }



    /**
     * A single place to customize the response body of all Exception types.
     */
    protected ResponseEntity<ErrorMessage> handleExceptionInternal(Exception ex, ErrorMessage body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }

        return new ResponseEntity<>(body, headers, status);
    }


    public GlobalExceptionHandler(BindingResult result) {
        this.message = result.toString();
    }

    public GlobalExceptionHandler(Object object, String message) {
        this.object = object;
        this.message = message;

    }

    public GlobalExceptionHandler(RuntimeException exception) {
        this.message = exception.toString();
    }

    public GlobalExceptionHandler() {
    }

    @Override
    public String toString() {
        return "ChangeResultView{" + "message=" + message + ", object=" + object + '}';
    }


    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }


}


