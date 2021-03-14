package it.emant.auth.exception;

public class CustomException {
  public static class InvalidApiKey extends RuntimeException {
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
