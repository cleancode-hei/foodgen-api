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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class MealController {
  private final MealService mealService;
  private final MealMapper mealMapper;

  @GetMapping("/meals")
  public List<Meal> getMeals(
      HttpServletRequest request, @RequestParam(name = "region") String region) {
    return mealService.findMealsByCriteria(request, region).stream()
        .map(mealMapper::toDto)
        .collect(Collectors.toUnmodifiableList());
  }

  @GetMapping("/mealsByRating")
  public List<Meal> getMealsOrdered(
      @RequestParam("page") Integer page, @RequestParam("page_size") Integer pageSize) {
    List<Meal> meals =
        mealService.getMealByRating(page, pageSize).stream().map(mealMapper::toDto).toList();
    return meals;
  }

  @GetMapping("/meal/{id}")
  public Meal getMealById(@PathVariable String id) {
    return mealMapper.toDto(mealService.getMealById(id));
  }
}
