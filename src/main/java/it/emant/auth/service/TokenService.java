package it.emant.auth.service;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import javax.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import it.emant.auth.dto.TokenDTO;
import it.emant.auth.model.Key;
import it.emant.auth.model.Role;
import it.emant.auth.model.User;
import it.emant.auth.repository.KeyRepository;

@Service
public class TokenService {

  private static int VALIDITY_MS = 24 * 3600 * 1000;

  @Autowired
  private KeyRepository keyRepo;

  @Value("${token.secret}")
  private String SECRET_KEY;

  public TokenDTO getToken(String key) {
    Key storedKey = keyRepo
      .findByKey(key)
      .orElseThrow(RuntimeException::new);

    User user = storedKey.getUser();
    Set<Role> roles = storedKey.getRoles();

    long nowMillis = System.currentTimeMillis();
    
    Date now = new Date(nowMillis);
    Date expires = new Date(nowMillis + VALIDITY_MS);

    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);

    String jwt = Jwts.builder()
      .setSubject("key/" + storedKey.getId())
      .setIssuer("emant.altervista.it")
      .setExpiration(new Date(nowMillis + VALIDITY_MS))
      .setIssuedAt(now)
      .claim("name", user.getUsername())
      .claim("email", user.getEmail())
      .claim("roles", roles.stream()
        .map(r -> r.getName())
        .collect(Collectors.toList())
      )
      .signWith(
        SignatureAlgorithm.HS256,
        apiKeySecretBytes
      )
      .compact();
    
    return TokenDTO.builder()
      .token(jwt)
      .createdAt(now)
      .expiresAt(expires)
        .build();
  }
}
