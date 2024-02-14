package com.genfood.foodgenback.integration;

import static com.genfood.foodgenback.utils.UserUtils.UPDATED_USER3_USERNAME;
import static com.genfood.foodgenback.utils.UserUtils.USER1_USERNAME;
import static com.genfood.foodgenback.utils.UserUtils.auth4;
import static com.genfood.foodgenback.utils.UserUtils.authAdmin1;
import static com.genfood.foodgenback.utils.UserUtils.signUp4;
import static com.genfood.foodgenback.utils.UserUtils.updatedUser3;
import static com.genfood.foodgenback.utils.UserUtils.user1;

import com.genfood.foodgenback.conf.FacadeIT;
import com.genfood.foodgenback.endpoint.controller.UserController;
import com.genfood.foodgenback.endpoint.rest.model.Role;
import com.genfood.foodgenback.endpoint.rest.model.User;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@Slf4j
public class UserIT extends FacadeIT {
  @Autowired private UserController controller;
  MockHttpServletRequest request;

  @Test
  void read_user_by_id() {
    User actual = controller.getByUserName(USER1_USERNAME);
    Assertions.assertEquals(user1(), actual);
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
  void register() {
    Assertions.assertEquals(String.class, controller.signUp(signUp4()).getClass());
  }

  @Test
  void sign_in() {
    Assertions.assertEquals(String.class, controller.signIn(auth4()).getClass());
    Assertions.assertEquals(String.class, controller.signIn(auth4()).getClass());
  }

  @Test
  void sign_in_as_admin() {
    String token = controller.signIn(authAdmin1());
    request = new MockHttpServletRequest();
    request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    Assertions.assertEquals(Role.ADMIN, controller.whoami(request).getRole());
  }
}
