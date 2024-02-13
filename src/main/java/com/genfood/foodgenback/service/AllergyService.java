package com.genfood.foodgenback.service;

import com.genfood.foodgenback.repository.AllergyRepository;
import com.genfood.foodgenback.repository.IngredientRepository;
import com.genfood.foodgenback.repository.UserRepository;
import com.genfood.foodgenback.repository.model.Allergy;
import com.genfood.foodgenback.repository.model.Ingredient;
import com.genfood.foodgenback.repository.model.User;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AllergyService {
  private final AllergyRepository allergyRepository;
  private final UserRepository userRepository;
  private final AuthService service;
  private final IngredientRepository ingredientRepository;

  public List<Allergy> getAllergies() {
    return allergyRepository.findAll();
  }

  public List<Allergy> findAllergyByUserId(String userId) {
    User user = userRepository.findById(userId).orElse(null);
    return allergyRepository.findByUser(user);
  }

  public List<Allergy> crUpdateAllergies(HttpServletRequest request, List<String> allergies) {
    User user = service.whoami(request);
    List<Allergy> allergyList = new ArrayList<>();
    for (String allergy : allergies) {
      List<Ingredient> ingredients =
          ingredientRepository.findAllByNameIsContainingIgnoreCase(allergy);
      if (ingredients.size() == 0) {
        continue;
      }
      for (Ingredient ingredient : ingredients) {
        Allergy entity = Allergy.builder().user(user).ingredient(ingredient).build();
        allergyRepository.save(entity);
        allergyList.add(entity);
      }
    }
    return allergyList;
  }
}
