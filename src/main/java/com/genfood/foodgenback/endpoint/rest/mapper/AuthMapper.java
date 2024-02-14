package com.genfood.foodgenback.endpoint.rest.mapper;

import com.genfood.foodgenback.repository.model.Ingredient;
import com.genfood.foodgenback.repository.model.User;
import com.genfood.foodgenback.repository.model.exception.NotFoundException;
import com.genfood.foodgenback.service.IngredientService;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuthMapper {
  private final IngredientService ingredientService;

  public com.genfood.foodgenback.repository.model.Allergy allergyToCreatableEntity(
      String allergy, User user) {
    Ingredient ingredient = ingredientService.getIngredientByName(allergy);
    if (Objects.isNull(ingredient)) {
      throw new NotFoundException("Ingredient of name: " + allergy + " not found.");
    }
    return com.genfood.foodgenback.repository.model.Allergy.builder()
        .user(user)
        .ingredient(ingredient)
        .build();
  }

  public com.genfood.foodgenback.repository.model.UserPreference userPreferenceToCreatableEntity(
      String userPreference, User user) {
    Ingredient ingredient = ingredientService.getIngredientByName(userPreference);
    if (Objects.isNull(ingredient)) {
      throw new NotFoundException("Ingredient of name: " + userPreference + " not found.");
    }
    return com.genfood.foodgenback.repository.model.UserPreference.builder()
        .user(user)
        .ingredient(ingredient)
        .build();
  }
}
