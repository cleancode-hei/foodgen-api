package com.genfood.foodgenback.repository;

import com.genfood.foodgenback.repository.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
  Optional<User> findByUsername(String name);

  User getById(String id);

  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);
}
