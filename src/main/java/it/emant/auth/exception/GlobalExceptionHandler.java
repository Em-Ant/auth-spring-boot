package it.emant.auth.exception;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import it.emant.auth.dto.ErrorDTO;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
  
  @ExceptionHandler(CustomException.ApiKeyNotFound.class)
  public ResponseEntity<ErrorDTO> handleKeyNotFpund(Exception ex, WebRequest req) {
    return createErrorResponse(ex, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(CustomException.InvalidApiKey.class)
  public ResponseEntity<ErrorDTO> handleInvalidKey(Exception ex, WebRequest req) {
    return createErrorResponse(ex, HttpStatus.UNAUTHORIZED);
  }
  
  private ResponseEntity<ErrorDTO> createErrorResponse(Exception ex, HttpStatus status) {
    String msg = Optional.of(ex)
        .map(Exception::getMessage)
        .orElse(status.name().toLowerCase());

    return new ResponseEntity<ErrorDTO>(
      new ErrorDTO(status.value(), msg),
        status
      );
  }
}

