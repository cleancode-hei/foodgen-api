package com.genfood.foodgenback.utils;

import static com.genfood.foodgenback.utils.IngredientUtils.ig1;
import static com.genfood.foodgenback.utils.IngredientUtils.ig2;
import static com.genfood.foodgenback.utils.IngredientUtils.ig3;
import static com.genfood.foodgenback.utils.UserUtils.user1;
import static com.genfood.foodgenback.utils.UserUtils.user2;
import static com.genfood.foodgenback.utils.UserUtils.user3;

import com.genfood.foodgenback.endpoint.rest.model.Allergy;

public class AllergyUtils {
  public static final String ALLERGY1_ID = "allergy1_id";
  public static final String ALLERGY2_ID = "allergy2_id";
  public static final String ALLERGY3_ID = "allergy3_id";

  public static Allergy allergy1() {
    return Allergy.builder().id(ALLERGY1_ID).user(user1()).ingredient(ig1()).build();
  }

  public static Allergy allergy2() {
    return Allergy.builder().id(ALLERGY2_ID).user(user2()).ingredient(ig2()).build();
  }

  public static Allergy allergy3() {
    return Allergy.builder().id(ALLERGY3_ID).user(user3()).ingredient(ig3()).build();
  }
}
