package com.genfood.foodgenback.endpoint.rest.mapper;

import com.genfood.foodgenback.endpoint.rest.model.User;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserMapper {
  private final PasswordEncoder encoder;

  public User toDto(com.genfood.foodgenback.repository.model.User entity) {
    return User.builder()
        .id(entity.getId())
        .email(entity.getEmail())
        .username(entity.getUsername())
        .password(entity.getPassword())
        .firstname(entity.getFirstname())
        .lastname(entity.getLastname())
        .role(entity.getRole())
        .build();
  }

  public com.genfood.foodgenback.repository.model.User toEntity(User dto) {
    return com.genfood.foodgenback.repository.model.User.builder()
        .id(dto.getId())
        .lastname(dto.getLastname())
        .firstname(dto.getFirstname())
        .email(dto.getEmail())
        .username(dto.getUsername())
        .role(dto.getRole())
        .password(encoder.encode(dto.getPassword()))
        .build();
  }
}
