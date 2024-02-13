package com.genfood.foodgenback.repository;

import com.genfood.foodgenback.repository.model.Ingredient;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, String> {
  boolean existsByName(String name);

  List<Ingredient> findAllByNameIsContainingIgnoreCase(String name);
}
