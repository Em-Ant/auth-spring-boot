package it.emant.auth.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import it.emant.auth.model.Roles;
import it.emant.auth.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
  public Optional<Role> findByName(Roles name);
}
