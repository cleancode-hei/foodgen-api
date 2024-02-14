package com.genfood.foodgenback.endpoint.rest.mapper;

import com.genfood.foodgenback.endpoint.rest.model.Allergy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AllergyMapper {

  public Allergy toDto(com.genfood.foodgenback.repository.model.Allergy entity) {
    return Allergy.builder()
        .id(entity.getId())
        .ingredientName(entity.getIngredient().getName())
        .build();
  }
}
