package it.emant.auth.service;
import java.util.Date;
import java.util.List;
import javax.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import it.emant.auth.dto.TokenDTO;

@Service
public class TokenService {

  private static int VALIDITY_MS = 24 * 3600 * 1000;

  @Value("${token.secret}")
  private String SECRET_KEY;

  public TokenDTO createToken(String user, List<String> roles)  {
    
    long nowMillis = System.currentTimeMillis();
    
    Date now = new Date(nowMillis);
    Date expires = new Date(nowMillis + VALIDITY_MS);

    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);

    String jwt = Jwts.builder()
      .setSubject("token test")
      .setIssuer("emant.altervista.it")
      .setExpiration(new Date(nowMillis + VALIDITY_MS))
      .setIssuedAt(now)
      .claim("name", user)
      .claim("roles", roles)
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

  public Claims validateToken(String token) {
   Claims claims = Jwts.parser()         
       .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
       .parseClaimsJws(token).getBody();
   return claims;
  }
}
