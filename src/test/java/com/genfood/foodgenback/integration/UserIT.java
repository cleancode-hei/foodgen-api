package com.genfood.foodgenback.integration;

import com.genfood.foodgenback.conf.FacadeIT;
import com.genfood.foodgenback.endpoint.controller.UserController;
import com.genfood.foodgenback.endpoint.rest.model.Role;
import com.genfood.foodgenback.endpoint.rest.model.User;
import com.genfood.foodgenback.repository.model.exception.BadRequestException;
import com.genfood.foodgenback.repository.model.exception.NotFoundException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.BadCredentialsException;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.genfood.foodgenback.utils.UserUtils.*;

@Testcontainers
@Slf4j
public class UserIT extends FacadeIT {
  @Autowired private UserController controller;
  MockHttpServletRequest request;

  @Test
  void read_user_by_id() {
    User actual = controller.getByUserName(USER2_USERNAME);
    Assertions.assertEquals(user2(), actual);
  }
  @Test
  void should_throw_not_found() {
    NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> controller.getByUserName("coco"));
    Assertions.assertEquals("User name with coco not found", exception.getMessage());
  }

  @Test
  void crupdate_user() {
    controller.crupdateUsers(List.of(updatedUser3()));
    User actual = controller.getByUserName(UPDATED_USER3_USERNAME);
    Assertions.assertEquals(updatedUser3().getId(), actual.getId());
    Assertions.assertEquals(updatedUser3().getFirstname(), actual.getFirstname());
    Assertions.assertEquals(updatedUser3().getLastname(), actual.getLastname());
    Assertions.assertEquals(updatedUser3().getEmail(), actual.getEmail());
    Assertions.assertEquals(updatedUser3().getUsername(), actual.getUsername());
    Assertions.assertEquals(updatedUser3().getRole(), actual.getRole());
  }
  @Test
  void should_throw_email_error(){
    BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () ->{
      controller.crupdateUsers(List.of(User.builder()
              .id(USER2_ID)
              .username(USER1_USERNAME)
              .firstname(USER1_FIRSTNAME)
              .lastname(USER1_LASTNAME)
              .email(USER1_EMAIL)
              .password(USER1_PASSWORD)
              .role(Role.valueOf(USER1_ROLE))
              .build())
      );
    });
    Assertions.assertEquals("Mail address already taken,try other",exception.getMessage());
  }


  @Test
  void register() {
    Assertions.assertEquals(String.class, controller.signUp(signUp4()).getClass());
  }
  @Test
  void should_not_register() {
    DuplicateKeyException exception = Assertions.assertThrows(DuplicateKeyException.class, () ->{controller.signUp(signUp1());});
    Assertions.assertEquals("User with the email address: "+ signUp1().getEmail() +" already exists.",exception.getMessage());
  }

  @Test
  void should_throw_bad_credentials(){
    BadCredentialsException exception = Assertions.assertThrows(BadCredentialsException.class, () -> {controller.signIn(auth1());});
    Assertions.assertEquals("Wrong Password!",exception.getMessage());
  }

  @Test
  void sign_in() {
    Assertions.assertEquals(String.class, controller.signIn(auth4()).getClass());
  }

  @Test
  void sign_in_as_admin() {
    String token = controller.signIn(authAdmin1());
    request = new MockHttpServletRequest();
    request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    Assertions.assertEquals(Role.ADMIN, controller.whoami(request).getRole());
    Assertions.assertEquals(String.class, controller.signIn(auth4()).getClass());
  }
}
