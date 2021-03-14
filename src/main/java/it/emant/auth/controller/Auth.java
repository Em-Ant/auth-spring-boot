package it.emant.auth.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import it.emant.auth.dto.TokenDTO;
import it.emant.auth.exception.CustomException;
import it.emant.auth.service.TokenService;

@RestController
@RequestMapping("/auth")
public class Auth {

  @Autowired
  TokenService tokenService;

  @GetMapping(value = {"/", ""}, produces = MediaType.APPLICATION_JSON_VALUE)
  public TokenDTO get(@RequestHeader(value="x-api-key", required=false) String key)
      throws CustomException.ApiKeyNotFound {
      
    return Optional.ofNullable(key)
      .map(tokenService::getToken)
      .orElseThrow(CustomException.ApiKeyNotFound::new);
  }
}