package com.genfood.foodgenback.endpoint.controller;

import com.genfood.foodgenback.endpoint.rest.mapper.MealMapper;
import com.genfood.foodgenback.endpoint.rest.model.Meal;
import com.genfood.foodgenback.service.MealService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class MealController {
  private final MealService mealService;
  private final MealMapper mealMapper;

  @GetMapping("/meals")
  public List<Meal> getMeals(HttpServletRequest request) {
    return mealService.getRandomMeals(request).stream().map(mealMapper::toDto).toList();
  }

  @PostMapping("/meals/download/{id}")
  public void downloadMeal(@PathVariable String id) {
    mealService.updateMealDownloadNumber(id);
  }

  @GetMapping("/recommendedMeals")
  public List<Meal> getMealsByPreferences(
      HttpServletRequest request,
      @RequestParam("page") Integer page,
      @RequestParam("page_size") Integer pageSize) {
    return mealService.getMealByPreferences(request, page, pageSize).stream()
        .map(mealMapper::toDto)
        .toList();
  }

  @GetMapping("/mealsByCriteria")
  public List<Meal> getMealsByCriteria(
      HttpServletRequest request,
      @RequestParam(name = "region_name", required = false) String regionName,
      @RequestParam(name = "ingredients", required = false) List<String> ingredients) {
    List<Meal> meals =
        mealService.getMealsByCriteria(regionName, ingredients, request).stream()
            .map(mealMapper::toDto)
            .collect(Collectors.toUnmodifiableList());
    return meals;
  }

  @GetMapping("/mealsByRating")
  public List<Meal> getMealsOrderedByDownload(
      @RequestParam("page") Integer page, @RequestParam("page_size") Integer pageSize) {
    return mealService.getMealByDownload(page, pageSize).stream().map(mealMapper::toDto).toList();
  }

  @GetMapping("/meal/{id}")
  public Meal getMealById(@PathVariable String id) {
    return mealMapper.toDto(mealService.getMealById(id));
  }
}
