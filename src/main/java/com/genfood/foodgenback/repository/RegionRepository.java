package com.genfood.foodgenback.repository;

import com.genfood.foodgenback.repository.model.Region;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<Region, String> {
  boolean existsByName(String name);

  Optional<Region> findByName(String name);
}
