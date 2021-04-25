package it.emant.auth.security;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import io.jsonwebtoken.Claims;
import it.emant.auth.service.TokenService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

  private final TokenService tokenService;

  public JwtAuthorizationFilter(AuthenticationManager authManager, TokenService tokenService) {
      super(authManager);
      this.tokenService = tokenService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest req,
      HttpServletResponse res,
      FilterChain chain) throws IOException, ServletException {
  
    try {
      String token = Optional.ofNullable(req.getHeader("AUthorization"))
          .map(t -> t.replace("Bearer ", "")).orElse(null);


      Claims claims = tokenService.validateToken(token);
      String username = claims.get("name", String.class);
      @SuppressWarnings("unchecked")
      List<String> roles = claims.get("roles", List.class);

      List<SimpleGrantedAuthority> authorities = roles.stream()
          .map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());

      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(username, null, authorities);
      SecurityContextHolder.getContext().setAuthentication(authentication);

    } catch (RuntimeException e) {
      log.debug(e.toString());
    };
    chain.doFilter(req, res);
  }
}
