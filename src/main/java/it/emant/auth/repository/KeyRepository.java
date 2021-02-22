package it.emant.auth.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import it.emant.auth.model.Key;

@Repository
public interface KeyRepository extends JpaRepository<Key, Long> {
  public Optional<Key> findByKey(String key);
}
