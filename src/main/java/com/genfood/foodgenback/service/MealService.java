package com.genfood.foodgenback.service;

import com.genfood.foodgenback.repository.MealRepository;
import com.genfood.foodgenback.repository.model.Meal;
import com.genfood.foodgenback.repository.model.User;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MealService {
  private final MealRepository mealRepository;
  private final AuthService service;

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
    User user = service.whoami(request);
    Pageable pageable = PageRequest.of(page, pageSize);
    return mealRepository.findMealsByPreferences(user.getId(), pageable);
  }

  public List<Meal> getRandomMeals(HttpServletRequest request) {
    User user = service.whoami(request);
    return mealRepository.findMealsWithoutAllergies(user.getId());
  }
}
