package it.emant.auth.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles")
@NoArgsConstructor
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Getter
  @Setter
  private Integer id;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  @Getter
  @Setter
  private ERole name;

  public Role(ERole name) {
    this.name = name;
  }

}
