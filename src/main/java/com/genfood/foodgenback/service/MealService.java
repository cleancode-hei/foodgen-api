package com.genfood.foodgenback.service;

import com.genfood.foodgenback.endpoint.rest.mapper.RecipeIngredientMapper;
import com.genfood.foodgenback.endpoint.rest.model.Ingredients;
import com.genfood.foodgenback.repository.MealRepository;
import com.genfood.foodgenback.repository.model.Allergy;
import com.genfood.foodgenback.repository.model.Meal;
import com.genfood.foodgenback.repository.model.User;
import com.genfood.foodgenback.repository.model.UserPreference;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MealService {

  private final MealRepository mealRepository;
  private final RecipeIngredientService recipeIngredientService;
  private final AuthService service;
  private final AllergyService allergyService;
  private final RecipeIngredientMapper recipeIngredientMapper;
  private final UserPreferencesService userPreferencesService;

  public Meal getMealById(String id) {
    return mealRepository.findById(id).get();
  }

  public List<Meal> getMealByRating(Integer page, Integer pageSize) {
    Pageable pageable = PageRequest.of(page, pageSize);
    return mealRepository.findAllOrderByDownload(pageable);
  }

  public Meal getMealByPreferences(User user, Meal meal) {
    List<UserPreference> preferences = userPreferencesService.getPreferencesByUser(user);
    List<Ingredients> mealIngredients =
        recipeIngredientMapper
            .toDto(recipeIngredientService.getAllByRecipeId(meal.getRecipe().getId()))
            .getIngredients();
    for (UserPreference userPreference : preferences) {
      for (int i = 0; i < mealIngredients.size(); i++) {
        if (userPreference.getIngredient().getName() == mealIngredients.get(i).getName()) {
          return meal;
        }
      }
    }
    return new Meal();
  }

  public Meal getMealWithoutAllergy(User user, Meal meal) {
    List<Allergy> allergies = allergyService.findAllergyByUserId(user.getId());
    List<Ingredients> mealIngredients =
        recipeIngredientMapper
            .toDto(recipeIngredientService.getAllByRecipeId(meal.getRecipe().getId()))
            .getIngredients();
    for (Allergy allergy : allergies) {
      for (int i = 0; i < mealIngredients.size(); i++) {
        if (allergy.getIngredient().equals(mealIngredients.get(i))) {
          return new Meal();
        }
      }
    }
    return meal;
  }

  public List<Meal> getRandomMeals(HttpServletRequest request) {
    User user = service.whoami(request);
    List<UserPreference> preferences = userPreferencesService.getPreferencesByUser(user);
    List<Allergy> allergies = allergyService.findAllergyByUserId(user.getId());
    List<Meal> meals = new ArrayList<>();
    if (preferences.size() == 0 && allergies.size() == 0) {
      meals.add(mealRepository.findMealRandomly());
      meals.add(mealRepository.findMealRandomly());
      meals.add(mealRepository.findMealRandomly());
    } else if (preferences.size() > 0 && allergies.size() == 0) {
      while (meals.size() < 3) {
        Meal meal = getMealByPreferences(user, mealRepository.findMealRandomly());
        if (meal.getId() != null) {
          meals.add(meal);
        }
      }
    } else if (preferences.size() == 0) {
      while (meals.size() < 3) {
        Meal mealByPreferences = getMealByPreferences(user, mealRepository.findMealRandomly());
        if (mealByPreferences.getId() != null) {
          Meal mealWoutAllergy = getMealWithoutAllergy(user, mealByPreferences);
          if (mealWoutAllergy != null) {
            meals.add(mealWoutAllergy);
          }
        }
      }
    }
    return meals;
  }

  public List<Meal> findMealsByRegion(String regionName) {
    return mealRepository.findMealsByRegions(regionName);
  }
}
