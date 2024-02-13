package com.genfood.foodgenback.repository;

import com.genfood.foodgenback.repository.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, String> {
  boolean existsByName(String name);

  Optional<Region> findByName(String name);


}
