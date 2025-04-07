package edu.tcu.cs.tcusportsbroadcasting.common.exception;

import edu.tcu.cs.tcusportsbroadcasting.common.ApiResponse;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.exception.DuplicateEmailException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return new ApiResponse(false, 400, "Provided arguments are invalid, see data for details.", errors);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleDuplicateEmailException(DuplicateEmailException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("email", ex.getMessage());

        return new ApiResponse(false, 400, "Provided arguments are invalid, see data for details.", error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleMalformedJson(HttpMessageNotReadableException ex) {
        return new ApiResponse(false, 400, "Malformed JSON request.", null);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse handleAllOtherExceptions(Exception ex) {
        return new ApiResponse(false, 500, "Internal server error.", ex.getMessage());
    }
}
