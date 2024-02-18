package com.genfood.foodgenback.integration;

import static com.genfood.foodgenback.utils.IngredientUtils.IG1_ID;
import static com.genfood.foodgenback.utils.IngredientUtils.ig1;
import static com.genfood.foodgenback.utils.IngredientUtils.ig2;
import static com.genfood.foodgenback.utils.IngredientUtils.ig3;
import static com.genfood.foodgenback.utils.IngredientUtils.updatedIg3;

import com.genfood.foodgenback.conf.FacadeIT;
import com.genfood.foodgenback.endpoint.controller.IngredientController;
import com.genfood.foodgenback.endpoint.rest.model.Ingredient;
import com.genfood.foodgenback.repository.model.exception.ApiException;
import com.genfood.foodgenback.repository.model.exception.BadRequestException;
import com.genfood.foodgenback.repository.model.exception.NotFoundException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@Slf4j
public class IngredientIT extends FacadeIT {
  public static final int PAGE = 0;
  public static final int PAGE_SIZE = 10;
  @Autowired private IngredientController ingredientController;

  @Test
  void read_ingredients() {
    List<Ingredient> actual = ingredientController.getIngredients(PAGE, PAGE_SIZE);
    Assertions.assertTrue(actual.contains(ig1()));
    Assertions.assertTrue(actual.contains(ig2()));
    int expected = 3;
    Assertions.assertEquals(expected, actual.size());
  }

  @Test
  void read_ingredient_by_id() {
    Ingredient actual = ingredientController.getIngredientById(IG1_ID);
    Assertions.assertEquals(ig1(), actual);
  }

  @Test
  void should_throw_error_not_found() {
    String id = "fakeig";
    NotFoundException exception =
        Assertions.assertThrows(
            NotFoundException.class, () -> ingredientController.getIngredientById(id));
    Assertions.assertEquals("Ingredient of id: " + id + " not found.", exception.getMessage());
    Assertions.assertEquals(ApiException.ExceptionType.CLIENT_EXCEPTION, exception.getType());
  }

  @Test
  void crupdate_ingredients() {
    ingredientController.crupdateIngredients(List.of(ig1(), ig2(), ig3()));
    ingredientController.crupdateIngredients(List.of(updatedIg3()));
    List<Ingredient> actual = ingredientController.getIngredients(PAGE, PAGE_SIZE);
    Assertions.assertEquals(updatedIg3(), actual.get(2));
  }

  @Test
  void should_pass_validator() {
    BadRequestException exception =
        Assertions.assertThrows(
            BadRequestException.class,
            () ->
                ingredientController.crupdateIngredients(
                    List.of(Ingredient.builder().name(null).build())));
    Assertions.assertEquals("Name is mandatory", exception.getMessage());
    Assertions.assertEquals(ApiException.ExceptionType.CLIENT_EXCEPTION, exception.getType());
  }
}
