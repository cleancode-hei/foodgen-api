package com.genfood.foodgenback.endpoint.rest.mapper;

import com.genfood.foodgenback.endpoint.rest.model.Ingredient;
import org.springframework.stereotype.Component;

@Component
public class IngredientMapper {
  public Ingredient toDto(com.genfood.foodgenback.repository.model.Ingredient entity) {
    return Ingredient.builder().id(entity.getId()).name(entity.getName()).build();
  }

  public com.genfood.foodgenback.repository.model.Ingredient toEntity(Ingredient dto) {
    return com.genfood.foodgenback.repository.model.Ingredient.builder()
        .id(dto.getId())
        .name(dto.getName())
        .build();
  }
}
