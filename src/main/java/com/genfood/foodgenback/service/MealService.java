package com.genfood.foodgenback.service;

import com.genfood.foodgenback.repository.JDBCQueries;
import com.genfood.foodgenback.repository.MealRepository;
import com.genfood.foodgenback.repository.model.Meal;
import com.genfood.foodgenback.repository.model.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MealService {
  private final MealRepository mealRepository;
  private final UserService userService;
  private final AllergyService allergyService;
  private final JDBCQueries jdbcQueries;

  public Meal getMealById(String id) {
    return mealRepository.findById(id).get();
  }

  public List<Meal> getMealByDownload(Integer page, Integer pageSize) {
    Pageable pageable = PageRequest.of(page, pageSize);
    return mealRepository.findAllOrderByDownload(pageable);
  }

  public void updateMealDownloadNumber(String mealId) {
    Meal meal = mealRepository.findById(mealId).get();
    meal.setDownload(meal.getDownload() + 1);
    mealRepository.save(meal);
  }

  public List<Meal> getMealByPreferences(
      HttpServletRequest request, Integer page, Integer pageSize) {
    User user = userService.whoami(request);
    Pageable pageable = PageRequest.of(page, pageSize);
    return mealRepository.findMealsByPreferences(user.getId(), pageable);
  }

  public List<Meal> getRandomMeals(HttpServletRequest request) {
    User user = userService.whoami(request);
    return mealRepository.findMealsWithoutAllergies(user.getId());
  }

  public List<Meal> getMealsByCriteria(
      String regionName, List<String> ingredientsNames, HttpServletRequest request) {
    User user = userService.whoami(request);
    List<String> allergiesNames =
        allergyService.findAllergiesByUserId(user.getId()).stream()
            .map((allergy -> allergy.getIngredient().getName()))
            .collect(Collectors.toUnmodifiableList());
    return jdbcQueries.findAllMealsByCriterias(regionName, allergiesNames, ingredientsNames);
  }
}
