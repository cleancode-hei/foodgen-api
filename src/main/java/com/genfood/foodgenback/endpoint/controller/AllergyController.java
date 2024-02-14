package com.genfood.foodgenback.endpoint.controller;

import com.genfood.foodgenback.endpoint.rest.mapper.AllergyMapper;
import com.genfood.foodgenback.endpoint.rest.model.Allergy;
import com.genfood.foodgenback.service.AllergyService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class AllergyController {
  private final AllergyService allergyService;
  private final AllergyMapper allergyMapper;

  @GetMapping("/allergies")
  public List<Allergy> getAllergies() {
    return allergyService.getAllergies().stream()
        .map(allergyMapper::toDto)
        .collect(Collectors.toUnmodifiableList());
  }

  @GetMapping("/allergies/{user_id}")
  public List<Allergy> findAllergyByUserId(@PathVariable(name = "user_id") String userId) {
    return allergyService.findAllergiesByUserId(userId).stream()
        .map(allergyMapper::toDto)
        .collect(Collectors.toUnmodifiableList());
  }

  @PutMapping("/allergies")
  public List<Allergy> crupdateAllergies(
      HttpServletRequest request, @RequestBody List<String> allergies) {
    return allergyService.crUpdateAllergies(request, allergies).stream()
        .map(allergyMapper::toDto)
        .collect(Collectors.toUnmodifiableList());
  }
}
