package it.emant.auth.dto;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenDTO {
  private String token;
  private Date createdAt;
  private Date expiresAt;
}
