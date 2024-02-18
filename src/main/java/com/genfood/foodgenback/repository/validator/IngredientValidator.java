package com.genfood.foodgenback.repository.validator;

import com.genfood.foodgenback.repository.model.Ingredient;
import com.genfood.foodgenback.repository.model.exception.BadRequestException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class IngredientValidator implements Consumer<Ingredient> {
  public void accept(List<Ingredient> ingredients) {
    ingredients.forEach(this::accept);
  }

  @Override
  public void accept(Ingredient ingredient) {
    Set<String> violationMessages = new HashSet<>();
    if (ingredient.getName() == null) {
      violationMessages.add("Name is mandatory");
    }
    if (!violationMessages.isEmpty()) {
      String formattedViolationMessages =
          violationMessages.stream().map(String::toString).collect(Collectors.joining("\n"));
      throw new BadRequestException(formattedViolationMessages);
    }
  }
}
