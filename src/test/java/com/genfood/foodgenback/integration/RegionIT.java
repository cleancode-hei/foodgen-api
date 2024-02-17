package com.genfood.foodgenback.integration;

import static com.genfood.foodgenback.utils.RegionUtils.REGION1_ID;
import static com.genfood.foodgenback.utils.RegionUtils.region1;
import static com.genfood.foodgenback.utils.RegionUtils.region2;
import static com.genfood.foodgenback.utils.RegionUtils.updatedRegion3;

import com.genfood.foodgenback.conf.FacadeIT;
import com.genfood.foodgenback.endpoint.controller.RegionController;
import com.genfood.foodgenback.endpoint.rest.model.Region;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@Slf4j
public class RegionIT extends FacadeIT {
  public static final int PAGE = 0;
  public static final int PAGE_SIZE = 10;
  @Autowired private RegionController controller;

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
  void crupdate_regions() {
    controller.crupdateRegions(List.of(updatedRegion3()));
    List<Region> actual = controller.getRegions(PAGE, PAGE_SIZE);
    Assertions.assertEquals(updatedRegion3(), actual.get(2));
  }
}
