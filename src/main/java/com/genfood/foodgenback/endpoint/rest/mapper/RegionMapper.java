package com.genfood.foodgenback.endpoint.rest.mapper;

import com.genfood.foodgenback.endpoint.rest.model.Region;
import org.springframework.stereotype.Component;

@Component
public class RegionMapper {
  public Region toDto(com.genfood.foodgenback.repository.model.Region entity) {
    return Region.builder().id(entity.getId()).name(entity.getName()).build();
  }

  public com.genfood.foodgenback.repository.model.Region toEntity(Region dto) {
    return com.genfood.foodgenback.repository.model.Region.builder()
        .id(dto.getId())
        .name(dto.getName())
        .build();
  }
}
