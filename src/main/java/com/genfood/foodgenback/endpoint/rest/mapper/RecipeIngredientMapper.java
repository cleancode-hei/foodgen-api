package com.genfood.foodgenback.endpoint.rest.mapper;

import com.genfood.foodgenback.endpoint.rest.model.Ingredient;
import com.genfood.foodgenback.endpoint.rest.model.RecipeIngredients;
import com.genfood.foodgenback.repository.model.RecipeIngredient;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RecipeIngredientMapper {
  private final RecipeMapper recipeMapper;
  private final IngredientMapper ingredientMapper;

  public RecipeIngredients toDto(List<RecipeIngredient> entity) {
    List<Ingredient> ingredients = new ArrayList<>();
    for (int i = 0; i < entity.size(); i++) {
      ingredients.add(ingredientMapper.toDto(entity.get(i).getIngredient()));
    }
    return RecipeIngredients.builder()
        .recipe(recipeMapper.toDto(entity.get(0).getRecipe()))
        .ingredients(ingredients.size() > 0 ? ingredients : null)
        .build();
  }
}
