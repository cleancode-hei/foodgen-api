package com.genfood.foodgenback.endpoint.controller;

import com.genfood.foodgenback.endpoint.rest.mapper.IngredientMapper;
import com.genfood.foodgenback.endpoint.rest.model.Ingredient;
import com.genfood.foodgenback.service.IngredientService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class IngredientController {
  private final IngredientService ingredientService;
  private final IngredientMapper ingredientMapper;

  @GetMapping("/ingredients")
  public List<Ingredient> getIngredients(
      @RequestParam("page") Integer page, @RequestParam("page_size") Integer pageSize) {
    List<Ingredient> ingredients =
        ingredientService.getIngredients(page, pageSize).stream()
            .map(ingredientMapper::toDto)
            .collect(Collectors.toUnmodifiableList());
    return ingredients;
  }

  @GetMapping("/ingredients/{id}")
  public Ingredient getIngredientById(@PathVariable String id) {
    Ingredient ingredient = ingredientMapper.toDto(ingredientService.getById(id));
    return ingredient;
  }

  @PutMapping("/ingredients")
  public List<Ingredient> crupdateIngredients(@RequestBody List<Ingredient> ingredients) {
    List<com.genfood.foodgenback.repository.model.Ingredient> toSave =
        ingredients.stream()
            .map(ingredientMapper::toEntity)
            .collect(Collectors.toUnmodifiableList());
    List<Ingredient> crupdated =
        ingredientService.saveIngredients(toSave).stream()
            .map(ingredientMapper::toDto)
            .collect(Collectors.toUnmodifiableList());
    return crupdated;
  }
}
