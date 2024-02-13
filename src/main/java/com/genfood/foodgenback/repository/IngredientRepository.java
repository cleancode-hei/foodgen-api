package com.genfood.foodgenback.repository;

import com.genfood.foodgenback.repository.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, String> {
  List<Ingredient> findAllByNameIsContainingIgnoreCase(String name);

  List<Ingredient> findIngredientsByNameIn(List<String> names);

  Ingredient findByName(String name);
}
