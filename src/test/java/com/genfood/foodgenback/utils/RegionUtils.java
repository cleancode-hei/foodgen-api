package com.genfood.foodgenback.utils;

import com.genfood.foodgenback.endpoint.rest.model.Region;

public class RegionUtils {
  public static final String REGION1_ID = "region1_id";
  public static final String REGION2_ID = "region2_id";
  public static final String REGION3_ID = "region3_id";
  public static final String REGION1_NAME = "region1_name";
  public static final String REGION2_NAME = "region2_name";
  public static final String REGION3_NAME = "region3_name";
  public static final String UPDATED_REGION3_NAME = "rg3_name";

  public static Region region1() {
    return Region.builder().id(REGION1_ID).name(REGION1_NAME).build();
  }

  public static Region region2() {
    return Region.builder().id(REGION2_ID).name(REGION2_NAME).build();
  }

  public static Region region3() {
    return Region.builder().id(REGION3_ID).name(REGION3_NAME).build();
  }

  public static Region updatedRegion3() {
    return Region.builder().id(REGION3_ID).name(UPDATED_REGION3_NAME).build();
  }
}
