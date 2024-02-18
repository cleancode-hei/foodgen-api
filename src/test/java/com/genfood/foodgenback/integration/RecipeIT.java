package com.genfood.foodgenback.integration;

import static com.genfood.foodgenback.repository.model.exception.ApiException.ExceptionType.CLIENT_EXCEPTION;
import static com.genfood.foodgenback.utils.RecipeUtils.RECIPE1_ID;
import static com.genfood.foodgenback.utils.RecipeUtils.recipe1;
import static com.genfood.foodgenback.utils.RecipeUtils.recipe2;
import static com.genfood.foodgenback.utils.RecipeUtils.recipeIngredients1;
import static com.genfood.foodgenback.utils.RecipeUtils.updatedRecipe3;

import com.genfood.foodgenback.conf.FacadeIT;
import com.genfood.foodgenback.endpoint.controller.RecipeController;
import com.genfood.foodgenback.endpoint.rest.model.Recipe;
import com.genfood.foodgenback.endpoint.rest.model.RecipeIngredients;
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
public class RecipeIT extends FacadeIT {

  public static final int PAGE = 0;
  public static final int PAGE_SIZE = 10;
  @Autowired private RecipeController controller;

  @Test
  void read_recipes() {
    List<Recipe> actual = controller.getRecipes(PAGE, PAGE_SIZE);
    Assertions.assertTrue(actual.contains(recipe1()));
    Assertions.assertTrue(actual.contains(recipe2()));
    Assertions.assertEquals(9, actual.size());
  }

  @Test
  void read_recipe_by_id() {
    RecipeIngredients actual = controller.getRecipeById(RECIPE1_ID);
    Assertions.assertEquals(recipeIngredients1(), actual);
  }

  @Test
  void should_throw_not_found() {
    String id = "fakerecipe";
    NotFoundException exception =
        Assertions.assertThrows(NotFoundException.class, () -> controller.getRecipeById(id));
    Assertions.assertEquals("Recipe with id: " + id + " not found", exception.getMessage());
    Assertions.assertEquals(CLIENT_EXCEPTION, exception.getType());
  }

  @Test
  void crupdate_recipes() {
    controller.crupdateRecipes(List.of(updatedRecipe3()));
    List<Recipe> actual = controller.getRecipes(PAGE, PAGE_SIZE);
    Assertions.assertTrue(actual.contains(updatedRecipe3()));
  }

  @Test
  void should_pass_validator() {
    BadRequestException exception =
        Assertions.assertThrows(
            BadRequestException.class,
            () ->
                controller.crupdateRecipes(
                    List.of(Recipe.builder().name(null).readme(null).build())));
    BadRequestException exception2 =
        Assertions.assertThrows(
            BadRequestException.class,
            () ->
                controller.crupdateRecipes(
                    List.of(Recipe.builder().name("recipe1_name").readme("readme").build())));
    Assertions.assertEquals("Readme is mandatory\nName is mandatory", exception.getMessage());
    Assertions.assertEquals(CLIENT_EXCEPTION, exception.getType());
    Assertions.assertEquals("This recipe already exists.", exception2.getMessage());
    Assertions.assertEquals(CLIENT_EXCEPTION, exception2.getType());
  }
}
