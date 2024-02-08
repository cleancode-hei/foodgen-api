package com.genfood.foodgenback.integration;

import static com.genfood.foodgenback.utils.AllergyUtils.allergy1;
import static com.genfood.foodgenback.utils.AllergyUtils.allergy2;
import static com.genfood.foodgenback.utils.UserUtils.USER1_ID;
import static com.genfood.foodgenback.utils.UserUtils.signUp4;

import com.genfood.foodgenback.conf.FacadeIT;
import com.genfood.foodgenback.endpoint.controller.AllergyController;
import com.genfood.foodgenback.endpoint.controller.UserController;
import com.genfood.foodgenback.endpoint.rest.mapper.AllergyMapper;
import com.genfood.foodgenback.endpoint.rest.mapper.UserMapper;
import com.genfood.foodgenback.endpoint.rest.model.Allergy;
import com.genfood.foodgenback.repository.AllergyRepository;
import com.genfood.foodgenback.repository.IngredientRepository;
import com.genfood.foodgenback.repository.UserRepository;
import com.genfood.foodgenback.repository.validator.MailValidator;
import com.genfood.foodgenback.service.AllergyService;
import com.genfood.foodgenback.service.AuthService;
import com.genfood.foodgenback.service.JWTService;
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
public class AllergyIT extends FacadeIT {
  AllergyController allergyController;
  AllergyService allergyService;
  UserController userController;
  AuthService authService;
  UserService userService;

  MockHttpServletRequest request;
  UserDetailsServiceImpl userDetailsService;
  @Autowired AllergyMapper allergyMapper;
  @Autowired AllergyRepository allergyRepository;
  @Autowired UserRepository userRepository;
  @Autowired IngredientRepository ingredientRepository;
  @Autowired PasswordEncoder passwordEncoder;
  @Autowired UserMapper userMapper;
  @Autowired MailValidator mailValidator;
  @Autowired JWTService jwtService;

  @BeforeEach
  void setUp() {
    userService = new UserService(userRepository, mailValidator);
    userDetailsService = new UserDetailsServiceImpl(userService);
    authService = new AuthService(userService, userDetailsService, jwtService, passwordEncoder);
    allergyService =
        new AllergyService(allergyRepository, userRepository, authService, ingredientRepository);
    allergyController = new AllergyController(allergyService, allergyMapper);
    userController = new UserController(userMapper, userService, authService);
  }

  @Test
  void read_allergies() {
    int expected = 4;
    List<Allergy> actual = allergyController.getAllergies();
    Assertions.assertTrue(actual.contains(allergy1()));
    Assertions.assertTrue(actual.contains(allergy2()));
    Assertions.assertEquals(expected, actual.size());
  }

  @Test
  void read_allergy_by_user_id() {
    List<Allergy> actual = allergyController.findAllergyByUserId(USER1_ID);
    List<Allergy> expected = List.of(allergy1());
    Assertions.assertEquals(expected, actual);
  }

  @Test
  void crupdate_allergy() {
    String token = userController.signUp(signUp4());
    request = new MockHttpServletRequest();
    request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    List<Allergy> actual = allergyController.crupdateAllergies(request, List.of("ig1"));
    Assertions.assertEquals(1, actual.size());
  }
}
