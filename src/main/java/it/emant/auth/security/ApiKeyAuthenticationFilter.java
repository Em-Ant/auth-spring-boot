package it.emant.auth.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import it.emant.auth.service.TokenService;

public class ApiKeyAuthenticationFilter 
  extends UsernamePasswordAuthenticationFilter {
  
    private TokenService tokenService;
    
    private AuthenticationManager authenticationManager;
    
    private ObjectMapper mapper = new ObjectMapper();

    public ApiKeyAuthenticationFilter(
        AuthenticationManager authenticationManager,
        TokenService tokenService
      ) {

      this.authenticationManager = authenticationManager;
      this.tokenService = tokenService;

        setFilterProcessesUrl("/auth"); 
    }

   @Override
   public Authentication attemptAuthentication(
    HttpServletRequest req,
    HttpServletResponse res) throws AuthenticationException {
  
        String key = getXApiKeyHeader(req);

        return authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
            null, key, new ArrayList<>())
        );
    }
    
    @Override
    protected void successfulAuthentication (
      HttpServletRequest req,
      HttpServletResponse res,
      FilterChain chain,
      Authentication auth
    ) throws IOException {
      
      List<String> roles =  auth.getAuthorities()
        .stream()
        .map(Object::toString)
          .collect(Collectors.toList());

      String body = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
        tokenService.createToken(auth.getPrincipal().toString(), roles)
      );

      res.getWriter().write(body);
      res.getWriter().flush();
    }

    private String getXApiKeyHeader(HttpServletRequest request) {
      return request.getHeader("x-api-key");
    }
}
