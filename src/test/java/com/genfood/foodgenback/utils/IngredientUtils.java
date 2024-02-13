package com.genfood.foodgenback.utils;

import com.genfood.foodgenback.endpoint.rest.model.Ingredient;

public class IngredientUtils {
  public static final String IG1_ID = "ig1_id";
  public static final String IG2_ID = "ig2_id";
  public static final String IG3_ID = "ig3_id";
  public static final String IG1_NAME = "ig1_name";
  public static final String IG2_NAME = "ig2_name";
  public static final String IG3_NAME = "ig3_name";
  public static final String UPDATED_IG3_NAME = "ingredient3_name";

  public static Ingredient ig1() {
    return Ingredient.builder().id(IG1_ID).name(IG1_NAME).build();
  }

  public static Ingredient ig2() {
    return Ingredient.builder().id(IG2_ID).name(IG2_NAME).build();
  }

  public static Ingredient ig3() {
    return Ingredient.builder().id(IG3_ID).name(IG3_NAME).build();
  }

  public static Ingredient updatedIg3() {
    return Ingredient.builder().id(IG3_ID).name(UPDATED_IG3_NAME).build();
  }
}
