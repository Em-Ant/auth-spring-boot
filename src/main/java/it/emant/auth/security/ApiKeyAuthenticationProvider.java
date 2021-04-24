package it.emant.auth.security;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import it.emant.auth.repository.KeyRepository;
import it.emant.auth.model.Key;
import it.emant.auth.model.Role;

@Component
public class ApiKeyAuthenticationProvider 
  implements AuthenticationProvider {
    
    @Autowired  
    KeyRepository keys;

    @Override
    public Authentication authenticate(Authentication authentication) 
        throws AuthenticationException {

      String attemptKey = Optional.ofNullable(authentication.getCredentials())
        .map(c -> c.toString())
        .orElseThrow(
            () -> new AuthenticationCredentialsNotFoundException("api key not found")
        );

      Key key = keys.findByKey(attemptKey)
        .orElseThrow(() -> new BadCredentialsException("invalid api key"));

      return new UsernamePasswordAuthenticationToken(
          key.getUser().getUsername(),
          key.getKey(),
          getUserAuthority(key.getRoles())
        );
    }
  
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
        return userRoles.stream()
          .map(role -> new SimpleGrantedAuthority(role.getName().toString()))
          .collect(Collectors.toList());
    }
}