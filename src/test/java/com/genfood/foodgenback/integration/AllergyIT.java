package com.genfood.foodgenback.integration;

import static com.genfood.foodgenback.utils.AllergyUtils.allergy1;
import static com.genfood.foodgenback.utils.AllergyUtils.allergy2;
import static com.genfood.foodgenback.utils.UserUtils.USER1_ID;
import static com.genfood.foodgenback.utils.UserUtils.signUp4;

import com.genfood.foodgenback.conf.FacadeIT;
import com.genfood.foodgenback.endpoint.controller.AllergyController;
import com.genfood.foodgenback.endpoint.controller.UserController;
import com.genfood.foodgenback.endpoint.rest.model.Allergy;
import com.genfood.foodgenback.repository.model.exception.ApiException;
import com.genfood.foodgenback.repository.model.exception.NotFoundException;
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
public class AllergyIT extends FacadeIT {
  MockHttpServletRequest request;
  @Autowired private AllergyController allergyController;
  @Autowired private UserController userController;

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
  void should_throw_not_found() {
    String id = "fakeuser";
    NotFoundException exception =
        Assertions.assertThrows(
            NotFoundException.class, () -> allergyController.findAllergyByUserId(id));
    Assertions.assertEquals("User of id: " + id + " does not exists", exception.getMessage());
    Assertions.assertEquals(ApiException.ExceptionType.CLIENT_EXCEPTION, exception.getType());
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
