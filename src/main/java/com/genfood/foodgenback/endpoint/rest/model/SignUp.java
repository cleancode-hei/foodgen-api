package com.genfood.foodgenback.endpoint.rest.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SignUp {
  private String username;
  private String email;
  private String password;
  private List<String> allergies;
  private List<String> preferences;
}
