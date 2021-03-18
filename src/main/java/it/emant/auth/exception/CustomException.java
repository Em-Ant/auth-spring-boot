package it.emant.auth.exception;

import org.springframework.security.core.AuthenticationException;

public class CustomException {
  public static class InvalidApiKey extends AuthenticationException {
    static final long serialVersionUID = 1L;

    public InvalidApiKey() {
      super("invalid api key");
    }
  }
  
    public static class ApiKeyNotFound extends RuntimeException {
    static final long serialVersionUID = 1L;
    public ApiKeyNotFound() {
      super("x-api-key header not found");
    }
  }
  
}
