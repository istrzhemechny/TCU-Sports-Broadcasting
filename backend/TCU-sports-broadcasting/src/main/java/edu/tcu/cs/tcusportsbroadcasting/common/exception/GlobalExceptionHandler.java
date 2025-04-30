package edu.tcu.cs.tcusportsbroadcasting.common.exception;

import edu.tcu.cs.tcusportsbroadcasting.availability.exception.AvailabilityAlreadyExistsException;
import edu.tcu.cs.tcusportsbroadcasting.availability.exception.AvailabilityGameNotFoundException;
import edu.tcu.cs.tcusportsbroadcasting.availability.exception.AvailabilityUserNotFoundException;
import edu.tcu.cs.tcusportsbroadcasting.common.ApiResponse;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.exception.CrewMemberNotFoundException;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.exception.DuplicateEmailException;
import edu.tcu.cs.tcusportsbroadcasting.gameschedule.exception.GameNotFoundException;
import edu.tcu.cs.tcusportsbroadcasting.gameschedule.exception.ScheduleNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;
import org.springframework.validation.BindingResult;



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
            System.err.println("Validation error on '" + fieldError.getField() + "': " + fieldError.getDefaultMessage());
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

    @ExceptionHandler(CrewMemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse handleCrewMemberNotFound(CrewMemberNotFoundException ex) {
        return new ApiResponse(false, 404, ex.getMessage(), null);
    }

    @ExceptionHandler(AvailabilityUserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse handleUserNotFound(AvailabilityUserNotFoundException ex) {
        return new ApiResponse(false, 404, ex.getMessage(), null);
    }

    @ExceptionHandler(AvailabilityGameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse handleGameNotFound(AvailabilityGameNotFoundException ex) {
        return new ApiResponse(false, 404, ex.getMessage(), null);
    }

    @ExceptionHandler(AvailabilityAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponse handleDuplicate(AvailabilityAlreadyExistsException ex) {
        return new ApiResponse(false, 409, ex.getMessage(), null);
    }

    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<ApiResponse> handleGameNotFound(GameNotFoundException ex) {
        ApiResponse response = new ApiResponse(false, 404, ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ScheduleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse handleScheduleNotFound(ScheduleNotFoundException ex) {
        return new ApiResponse(false, 404, ex.getMessage(), null);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiResponse> handleHandlerMethodValidationException(HandlerMethodValidationException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getAllErrors().forEach(error -> {
            if (error instanceof FieldError fieldError) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            } else {
                errors.put(error.getDefaultMessage(), error.getDefaultMessage());
            }
        });

        ApiResponse response = new ApiResponse(false, 400, "Provided arguments are invalid, see data for details.", errors);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse handleAuthenticationException(Exception ex) {
        return new ApiResponse(false, 401, "username or password is incorrect", ex.getMessage());
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse handleInsufficientAuthenticationException(InsufficientAuthenticationException ex) {
        return new ApiResponse(false, 401, "Login credentials are missing", ex.getMessage());
    }

    @ExceptionHandler(AccountStatusException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse handleAccountStatusException(AccountStatusException ex) {
        return new ApiResponse(false, 401, "User account is abnormal", ex.getMessage());
    }

    @ExceptionHandler(InvalidBearerTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse handleInvalidBearerTokenException(InvalidBearerTokenException ex) {
        return new ApiResponse(false, 401, "The access token provided is expired, revoked, malformed, or invalid for other reasons", ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse handleAccessDeniedException(AccessDeniedException ex) {
        return new ApiResponse(false, 403, "No permission", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse handleAllOtherExceptions(Exception ex) {
        return new ApiResponse(false, 500, "Internal server error.", ex.getMessage());
    }

}
