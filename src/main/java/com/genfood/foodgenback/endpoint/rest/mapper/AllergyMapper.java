package com.genfood.foodgenback.endpoint.rest.mapper;

import com.genfood.foodgenback.endpoint.rest.model.Allergy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AllergyMapper {
  private final UserMapper userMapper;
  private final IngredientMapper ingredientMapper;

  public Allergy toDto(com.genfood.foodgenback.repository.model.Allergy entity) {
    return Allergy.builder()
        .id(entity.getId())
        .user(userMapper.toDto(entity.getUser()))
        .ingredient(ingredientMapper.toDto(entity.getIngredient()))
        .build();
  }
}
