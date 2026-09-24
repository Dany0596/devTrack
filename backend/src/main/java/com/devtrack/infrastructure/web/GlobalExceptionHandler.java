package com.devtrack.infrastructure.web;

import com.devtrack.application.exception.ProjectNotFoundException;
import com.devtrack.application.exception.TaskNotFoundException;
import com.devtrack.application.exception.TimeEntryNotFoundException;
import com.devtrack.infrastructure.web.dto.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * Centralized exception-to-HTTP-response translation for all REST controllers.
 * <p>
 * Keeps error formatting (the {@link ApiError} shape) in one place instead of
 * duplicating try/catch blocks across controllers.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maps a missing-project lookup to {@code 404 Not Found}.
     *
     * @param ex the exception raised when a project could not be found
     * @return a {@code 404} response with error details
     */
    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ProjectNotFoundException ex) {
        ApiError body = ApiError.of(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    /**
     * Maps a missing-task lookup to {@code 404 Not Found}.
     *
     * @param ex the exception raised when a task could not be found
     * @return a {@code 404} response with error details
     */
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(TaskNotFoundException ex) {
        ApiError body = ApiError.of(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    /**
     * Maps a missing-time-entry lookup to {@code 404 Not Found}.
     *
     * @param ex the exception raised when a time entry could not be found
     * @return a {@code 404} response with error details
     */
    @ExceptionHandler(TimeEntryNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(TimeEntryNotFoundException ex) {
        ApiError body = ApiError.of(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    /**
     * Maps Bean Validation failures on {@code @RequestBody} arguments to
     * {@code 400 Bad Request}, including one message per invalid field.
     *
     * @param ex the exception raised by a failed {@code @Valid} check
     * @return a {@code 400} response listing every field validation error
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        List<String> details = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .toList();
        ApiError body = ApiError.of(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Validation failed",
                details
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Maps a domain invariant violation (e.g. an empty {@link com.devtrack.domain.Project} name
     * raised from within the domain model itself) to {@code 400 Bad Request}.
     *
     * @param ex the exception raised by a violated domain invariant
     * @return a {@code 400} response with the invariant violation message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex) {
        ApiError body = ApiError.of(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Fallback handler for any exception not covered by a more specific handler,
     * mapped to {@code 500 Internal Server Error}. The original exception message
     * is intentionally not leaked to the client.
     *
     * @param ex the unexpected exception
     * @return a {@code 500} response with a generic error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(Exception ex) {
        ApiError body = ApiError.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Unexpected error"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
