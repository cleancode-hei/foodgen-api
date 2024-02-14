package com.genfood.foodgenback.repository;

import com.genfood.foodgenback.repository.model.Ingredient;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, String> {
  List<Ingredient> findAllByNameIsContainingIgnoreCase(String name);

  Ingredient findByName(String name);
}
