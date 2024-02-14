package com.genfood.foodgenback.integration;

import static com.genfood.foodgenback.utils.MealUtils.MEAL1_ID;
import static com.genfood.foodgenback.utils.MealUtils.meal1;
import static com.genfood.foodgenback.utils.MealUtils.meal8;
import static com.genfood.foodgenback.utils.MealUtils.meal9;
import static com.genfood.foodgenback.utils.MealUtils.updatedDownloadMeal1;
import static com.genfood.foodgenback.utils.UserUtils.auth1;
import static com.genfood.foodgenback.utils.UserUtils.signUp4;
import com.genfood.foodgenback.conf.FacadeIT;
import com.genfood.foodgenback.endpoint.controller.MealController;
import com.genfood.foodgenback.endpoint.controller.UserController;
import com.genfood.foodgenback.endpoint.rest.mapper.MealMapper;
import com.genfood.foodgenback.endpoint.rest.mapper.UserMapper;
import com.genfood.foodgenback.endpoint.rest.model.Meal;
import com.genfood.foodgenback.repository.AllergyRepository;
import com.genfood.foodgenback.repository.IngredientRepository;
import com.genfood.foodgenback.repository.MealRepository;
import com.genfood.foodgenback.repository.UserRepository;
import com.genfood.foodgenback.repository.validator.MailValidator;
import com.genfood.foodgenback.service.AuthService;
import com.genfood.foodgenback.service.JWTService;
import com.genfood.foodgenback.service.MealService;
import com.genfood.foodgenback.service.UserDetailsServiceImpl;
import com.genfood.foodgenback.service.UserService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@Slf4j
public class MealIT extends FacadeIT {
  public static final int PAGE = 0;
  public static final int PAGE_SIZE = 5;
  MealController mealController;
  UserController userController;
  MealService mealService;
  AuthService authService;
  UserService userService;
  UserDetailsServiceImpl userDetailsService;

  MockHttpServletRequest request;

  @Autowired MealMapper mealMapper;
  @Autowired AllergyRepository allergyRepository;
  @Autowired UserMapper userMapper;
  @Autowired MealRepository mealRepository;
  @Autowired UserRepository userRepository;

  @Autowired IngredientRepository ingredientRepository;
  @Autowired MailValidator mailValidator;
  @Autowired PasswordEncoder passwordEncoder;
  @Autowired JWTService jwtService;

  @BeforeEach
  void setUp() {
    userService = new UserService(userRepository, mailValidator);
    userDetailsService = new UserDetailsServiceImpl(userService);
    authService = new AuthService(userService, userDetailsService, jwtService, passwordEncoder);
    userController = new UserController(userMapper, userService, authService);
    mealService = new MealService(mealRepository, authService);
    mealController = new MealController(mealService, mealMapper);
  }

  @Test
  void read_meals() {
    String token = userController.signUp(signUp4());
    request = new MockHttpServletRequest();
    request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    List<Meal> actual = mealController.getMeals(request);
    Assertions.assertEquals(3, actual.size());
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
