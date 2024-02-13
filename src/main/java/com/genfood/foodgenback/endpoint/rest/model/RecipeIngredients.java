package com.genfood.foodgenback.endpoint.rest.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RecipeIngredients {
  private Recipe recipe;
  private List<Ingredient> ingredients;
}
