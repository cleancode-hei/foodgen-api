package com.genfood.foodgenback.utils;

import static com.genfood.foodgenback.utils.RecipeUtils.recipe1;
import static com.genfood.foodgenback.utils.RecipeUtils.recipe2;
import static com.genfood.foodgenback.utils.RecipeUtils.recipe3;
import static com.genfood.foodgenback.utils.RecipeUtils.recipe4;
import static com.genfood.foodgenback.utils.RecipeUtils.recipe5;
import static com.genfood.foodgenback.utils.RecipeUtils.recipe6;
import static com.genfood.foodgenback.utils.RecipeUtils.recipe7;
import static com.genfood.foodgenback.utils.RecipeUtils.recipe8;
import static com.genfood.foodgenback.utils.RecipeUtils.recipe9;
import static com.genfood.foodgenback.utils.RegionUtils.region1;
import static com.genfood.foodgenback.utils.RegionUtils.region2;
import static com.genfood.foodgenback.utils.RegionUtils.region3;

import com.genfood.foodgenback.endpoint.rest.model.Meal;

public class MealUtils {

  public static final String MEAL1_ID = "meal1_id";
  public static final String MEAL2_ID = "meal2_id";
  public static final String MEAL3_ID = "meal3_id";
  public static final String MEAL4_ID = "meal4_id";
  public static final String MEAL5_ID = "meal5_id";
  public static final String MEAL6_ID = "meal6_id";
  public static final String MEAL7_ID = "meal7_id";
  public static final String MEAL8_ID = "meal8_id";
  public static final String MEAL9_ID = "meal9_id";
  public static final String MEAL1_NAME = "meal1_name";
  public static final String MEAL2_NAME = "meal2_name";
  public static final String MEAL3_NAME = "meal3_name";
  public static final String MEAL4_NAME = "meal4_name";
  public static final String MEAL5_NAME = "meal5_name";
  public static final String MEAL6_NAME = "meal6_name";
  public static final String MEAL7_NAME = "meal7_name";
  public static final String MEAL8_NAME = "meal8_name";
  public static final String MEAL9_NAME = "meal9_name";
  public static final String UPDATED_MEAL3_NAME = "ml3_name";
  public static final Integer MEAL1_DOWNLOAD = 10;
  public static final Integer MEAL2_DOWNLOAD = 5;
  public static final Integer MEAL3_DOWNLOAD = 1;
  public static final Integer MEAL4_DOWNLOAD = 0;
  public static final Integer MEAL5_DOWNLOAD = 2;
  public static final Integer MEAL6_DOWNLOAD = 3;
  public static final Integer MEAL7_DOWNLOAD = 4;
  public static final Integer MEAL8_DOWNLOAD = 6;
  public static final Integer MEAL9_DOWNLOAD = 7;

  public static Meal meal1() {
    return Meal.builder()
        .id(MEAL1_ID)
        .name(MEAL1_NAME)
        .image(null)
        .download(MEAL1_DOWNLOAD)
        .region(region1())
        .recipe(recipe1())
        .build();
  }

  public static Meal updatedDownloadMeal1() {
    return Meal.builder()
        .id(MEAL1_ID)
        .name(MEAL1_NAME)
        .image(null)
        .download(MEAL1_DOWNLOAD + 1)
        .region(region1())
        .recipe(recipe1())
        .build();
  }

  public static Meal meal2() {
    return Meal.builder()
        .id(MEAL2_ID)
        .name(MEAL2_NAME)
        .image(null)
        .download(MEAL2_DOWNLOAD)
        .region(region2())
        .recipe(recipe2())
        .build();
  }

  public static Meal meal3() {
    return Meal.builder()
        .id(MEAL3_ID)
        .name(MEAL3_NAME)
        .image(null)
        .download(MEAL3_DOWNLOAD)
        .region(region3())
        .recipe(recipe3())
        .build();
  }

  public static Meal meal4() {
    return Meal.builder()
        .id(MEAL4_ID)
        .name(MEAL4_NAME)
        .image(null)
        .download(MEAL4_DOWNLOAD)
        .region(region3())
        .recipe(recipe4())
        .build();
  }

  public static Meal meal5() {
    return Meal.builder()
        .id(MEAL5_ID)
        .name(MEAL5_NAME)
        .image(null)
        .download(MEAL5_DOWNLOAD)
        .region(region2())
        .recipe(recipe5())
        .build();
  }

  public static Meal meal6() {
    return Meal.builder()
        .id(MEAL6_ID)
        .name(MEAL6_NAME)
        .image(null)
        .download(MEAL6_DOWNLOAD)
        .region(region1())
        .recipe(recipe6())
        .build();
  }

  public static Meal meal7() {
    return Meal.builder()
        .id(MEAL7_ID)
        .name(MEAL7_NAME)
        .image(null)
        .download(MEAL7_DOWNLOAD)
        .region(region1())
        .recipe(recipe7())
        .build();
  }

  public static Meal meal8() {
    return Meal.builder()
        .id(MEAL8_ID)
        .name(MEAL8_NAME)
        .image(null)
        .download(MEAL8_DOWNLOAD)
        .region(region2())
        .recipe(recipe8())
        .build();
  }

  public static Meal meal9() {
    return Meal.builder()
        .id(MEAL9_ID)
        .name(MEAL9_NAME)
        .image(null)
        .download(MEAL9_DOWNLOAD)
        .region(region3())
        .recipe(recipe9())
        .build();
  }

  public static Meal updatedMeal3() {
    return Meal.builder()
        .id(MEAL3_ID)
        .name(UPDATED_MEAL3_NAME)
        .image(null)
        .download(MEAL3_DOWNLOAD)
        .region(region3())
        .recipe(recipe3())
        .build();
  }
}
