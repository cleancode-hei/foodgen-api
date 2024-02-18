package com.genfood.foodgenback.service;

import com.genfood.foodgenback.repository.RecipeIngredientRepository;
import com.genfood.foodgenback.repository.model.RecipeIngredient;
import com.genfood.foodgenback.repository.model.exception.NotFoundException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RecipeIngredientService {
  private final RecipeIngredientRepository repository;

  public List<RecipeIngredient> getAllByRecipeId(String id) {
    if (!repository.existsRecipeIngredientByRecipe_Id(id)) {
      throw new NotFoundException("Recipe with id: " + id + " not found");
    } else {
      return repository.findAllByRecipe_Id(id);
    }
  }
}
