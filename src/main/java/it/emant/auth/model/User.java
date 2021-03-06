package it.emant.auth.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users",
    uniqueConstraints = {@UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")})
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter
  @Setter
  private Long id;

  @NotBlank
  @Size(max = 20)
  @Getter
  @Setter
  private String username;

  @NotBlank
  @Email
  @Size(max = 50)
  @Getter
  @Setter
  private String email;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  @Getter
  @Setter
  private Set<Key> keys = new HashSet<>();

  public User(String username, String email) {
    this.username = username;
    this.email = email;
  }

}
