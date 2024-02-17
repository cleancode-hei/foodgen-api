package com.genfood.foodgenback.integration;

import com.genfood.foodgenback.conf.FacadeIT;
import com.genfood.foodgenback.endpoint.controller.MealController;
import com.genfood.foodgenback.endpoint.controller.UserController;
import com.genfood.foodgenback.endpoint.rest.mapper.MealMapper;
import com.genfood.foodgenback.endpoint.rest.model.Meal;
import com.genfood.foodgenback.service.JWTService;
import com.genfood.foodgenback.service.MealService;
import com.genfood.foodgenback.service.UserDetailsServiceImpl;
import com.genfood.foodgenback.utils.IngredientUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.stream.Collectors;

import static com.genfood.foodgenback.utils.MealUtils.MEAL1_ID;
import static com.genfood.foodgenback.utils.MealUtils.meal1;
import static com.genfood.foodgenback.utils.MealUtils.meal6;
import static com.genfood.foodgenback.utils.MealUtils.meal7;
import static com.genfood.foodgenback.utils.MealUtils.meal8;
import static com.genfood.foodgenback.utils.MealUtils.meal9;
import static com.genfood.foodgenback.utils.MealUtils.updatedDownloadMeal1;
import static com.genfood.foodgenback.utils.RegionUtils.REGION1_NAME;
import static com.genfood.foodgenback.utils.UserUtils.auth1;
import static com.genfood.foodgenback.utils.UserUtils.auth2;
import static com.genfood.foodgenback.utils.UserUtils.authAdmin1;
import static com.genfood.foodgenback.utils.UserUtils.signUp4;

@Testcontainers
@Slf4j
public class MealIT extends FacadeIT {
  public static final int PAGE = 0;
  public static final int PAGE_SIZE = 5;
  @Autowired private MealController mealController;
  @Autowired private MealService mealService;
  @Autowired private MealMapper mealMapper;
  @Autowired private UserController userController;
  @Autowired private UserDetailsServiceImpl userDetailsService;
  @Autowired JWTService jwtService;
  MockHttpServletRequest request;

  @Test
  void read_meals() {
    String token = userController.signUp(signUp4());
    request = new MockHttpServletRequest();
    request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    List<Meal> actual = mealController.getMeals(request);
    Assertions.assertEquals(3, actual.size());
  }

  @Test
  void read_meals_filtered_with_user2_allergy() {
    String token = userController.signIn(auth2());
    request = new MockHttpServletRequest();
    request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    List<Meal> actual =
        mealService
            .getMealsByCriteria(REGION1_NAME, List.of(IngredientUtils.IG1_NAME), request)
            .stream()
            .map(mealMapper::toDto)
            .collect(Collectors.toUnmodifiableList());
    System.out.println(actual);
    Assertions.assertEquals(1, actual.size());
    Assertions.assertTrue(actual.contains(meal7()));
  }

  @Test
  void read_meals_filtered_by_region_with_null_ingredients_and_allergies() {
    String token = userController.signIn(authAdmin1());
    request = new MockHttpServletRequest();
    request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    List<Meal> actual = mealController.getMealsByCriteria(request, REGION1_NAME, null);
    Assertions.assertTrue(actual.containsAll(List.of(meal7(), meal6())));
  }

  @Test
  void read_meals_filtered_with_null_ingredients_region_and_allergies() {
    String token = userController.signIn(authAdmin1());
    request = new MockHttpServletRequest();
    request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    List<Meal> actual = mealController.getMealsByCriteria(request, null, null);
    Assertions.assertEquals(9, actual.size());
  }

  @Test
  void read_meal_by_id() {
    Meal actual = mealController.getMealById(MEAL1_ID);
    Assertions.assertEquals(meal1(), actual);
  }

  @Test
  void read_meal_by_download() {
    List<Meal> actual = mealController.getMealsOrderedByDownload(PAGE, PAGE_SIZE);
    Assertions.assertEquals(5, actual.size());
    Assertions.assertEquals(actual.get(0), updatedDownloadMeal1());
    Assertions.assertEquals(actual.get(1), meal9());
    Assertions.assertEquals(actual.get(2), meal8());
  }

  @Test
  void read_meal_by_preferences() {
    String token =
        jwtService.generateToken(userDetailsService.loadUserByUsername(auth1().getEmail()));
    request = new MockHttpServletRequest();
    request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    List<Meal> actual = mealController.getMealsByPreferences(request, PAGE, 6);
    Assertions.assertEquals(6, actual.size());
  }

  @Test
  void updated_meal_download_number() {
    mealController.downloadMeal(MEAL1_ID);
    Meal meal = mealController.getMealById(MEAL1_ID);
    Assertions.assertEquals(updatedDownloadMeal1(), meal);
  }
}
