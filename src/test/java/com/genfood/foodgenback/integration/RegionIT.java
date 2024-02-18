package com.genfood.foodgenback.integration;

import static com.genfood.foodgenback.utils.RegionUtils.REGION1_ID;
import static com.genfood.foodgenback.utils.RegionUtils.region1;
import static com.genfood.foodgenback.utils.RegionUtils.region2;
import static com.genfood.foodgenback.utils.RegionUtils.updatedRegion3;

import com.genfood.foodgenback.conf.FacadeIT;
import com.genfood.foodgenback.endpoint.controller.RegionController;
import com.genfood.foodgenback.endpoint.rest.model.Region;
import com.genfood.foodgenback.repository.model.exception.ApiException;
import com.genfood.foodgenback.repository.model.exception.BadRequestException;
import com.genfood.foodgenback.repository.model.exception.ForbiddenException;
import com.genfood.foodgenback.repository.model.exception.NotFoundException;
import com.genfood.foodgenback.service.JWTService;
import com.genfood.foodgenback.service.UserDetailsServiceImpl;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@Slf4j
public class RegionIT extends FacadeIT {
  public static final int PAGE = 0;
  public static final int PAGE_SIZE = 10;
  MockHttpServletRequest request;
  @Autowired private RegionController controller;
  @Autowired private JWTService jwtService;
  @Autowired private UserDetailsServiceImpl userDetailsService;

  @Test
  void read_regions() {
    List<Region> actual = controller.getRegions(PAGE, PAGE_SIZE);
    Assertions.assertTrue(actual.contains(region1()));
    Assertions.assertTrue(actual.contains(region2()));
    int expected = 3;
    Assertions.assertEquals(expected, actual.size());
  }

  @Test
  void read_region_by_id() {
    Region actual = controller.getRegionById(REGION1_ID);
    Assertions.assertEquals(region1(), actual);
  }

  @Test
  void should_throw_not_found() {
    String id = "fakeregion";
    NotFoundException exception =
        Assertions.assertThrows(NotFoundException.class, () -> controller.getRegionById(id));
    Assertions.assertEquals("Region name with id: " + id + " not found", exception.getMessage());
    Assertions.assertEquals(ApiException.ExceptionType.CLIENT_EXCEPTION, exception.getType());
  }

  @Test
  void crupdate_regions() {
    UserDetails userDetails = userDetailsService.loadUserByUsername("user3@gmail.com");
    String token = jwtService.generateToken(userDetails);
    request = new MockHttpServletRequest();
    request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    controller.crupdateRegions(request, List.of(updatedRegion3()));
    List<Region> actual = controller.getRegions(PAGE, PAGE_SIZE);
    Assertions.assertEquals(updatedRegion3(), actual.get(2));
  }

  @Test
  void should_throw_forbidden_on_crupdate() {
    UserDetails userDetails = userDetailsService.loadUserByUsername("user1@gmail.com");
    String token = jwtService.generateToken(userDetails);
    request = new MockHttpServletRequest();
    request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    ForbiddenException exception =
        Assertions.assertThrows(
            ForbiddenException.class,
            () -> controller.crupdateRegions(request, List.of(updatedRegion3())));
    Assertions.assertEquals("You are not allowed to do this operation!", exception.getMessage());
    Assertions.assertEquals(ApiException.ExceptionType.CLIENT_EXCEPTION, exception.getType());
  }

  @Test
  void should_not_crupdate_regions() {
    UserDetails userDetails = userDetailsService.loadUserByUsername("user3@gmail.com");
    String token = jwtService.generateToken(userDetails);
    request = new MockHttpServletRequest();
    request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    BadRequestException exception =
        Assertions.assertThrows(
            BadRequestException.class,
            () -> {
              controller.crupdateRegions(
                  request, List.of(Region.builder().id(null).name(null).build()));
            });
    BadRequestException exception2 =
        Assertions.assertThrows(
            BadRequestException.class,
            () -> {
              controller.crupdateRegions(request, List.of(region1()));
            });
    Assertions.assertEquals("Name is mandatory", exception.getMessage());
    Assertions.assertEquals("This region already exists.", exception2.getMessage());
  }
}
