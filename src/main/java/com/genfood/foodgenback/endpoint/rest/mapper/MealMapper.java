package com.genfood.foodgenback.endpoint.rest.mapper;

import com.genfood.foodgenback.endpoint.rest.model.Meal;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MealMapper {
  private final RecipeMapper recipeMapper;
  private final RegionMapper regionMapper;

  public Meal toDto(com.genfood.foodgenback.repository.model.Meal entity) {
    return Meal.builder()
        .id(entity.getId())
        .name(entity.getName())
        .download(entity.getDownload())
        .image(entity.getImage() != null ? entity.getImage() : null)
        .recipe(recipeMapper.toDto(entity.getRecipe()))
        .region(regionMapper.toDto(entity.getRegion()))
        .build();
  }
}
