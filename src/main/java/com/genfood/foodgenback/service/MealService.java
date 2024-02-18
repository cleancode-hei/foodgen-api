package com.genfood.foodgenback.service;

import com.genfood.foodgenback.endpoint.rest.mapper.RecipeIngredientMapper;
import com.genfood.foodgenback.endpoint.rest.model.Ingredient;
import com.genfood.foodgenback.endpoint.rest.model.RecipeIngredients;
import com.genfood.foodgenback.repository.JDBCQueries;
import com.genfood.foodgenback.repository.MealRepository;
import com.genfood.foodgenback.repository.model.Meal;
import com.genfood.foodgenback.repository.model.User;
import jakarta.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import lombok.AllArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MealService {
  private final MealRepository mealRepository;
  private final UserService userService;
  private final AllergyService allergyService;
  private final RecipeIngredientService recipeIngredientService;
  private final RecipeIngredientMapper recipeIngredientMapper;
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

  public byte[] generatePDF(String mealId) throws IOException {
    Meal meal = mealRepository.findById(mealId).get();
    RecipeIngredients recipe =
        recipeIngredientMapper.toDto(
            recipeIngredientService.getAllByRecipeId(meal.getRecipe().getId()));
    String ingredients = "";
    for (Ingredient ingredient : recipe.getIngredients()) {
      ingredients += ingredient.getName() + ", ";
    }
    try (PDDocument document = new PDDocument()) {
      PDPage page = new PDPage();
      document.addPage(page);

      try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.newLineAtOffset(100, 700);
        if (meal.getImage().contains("http")) {
          BufferedImage bufferedImage = ImageIO.read(new URL(meal.getImage()));
          PDImageXObject pdImage = LosslessFactory.createFromImage(document, bufferedImage);
          float scale = 0.5f;
          contentStream.drawImage(
              pdImage, 100, 500, pdImage.getWidth() * scale, pdImage.getHeight() * scale);
        }
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Meal Recipe:");
        contentStream.newLineAtOffset(0, -16);
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.showText("Name: " + meal.getName());
        contentStream.newLineAtOffset(0, -17);
        contentStream.showText("Region: " + meal.getRegion().getName());
        contentStream.newLineAtOffset(0, -18);
        contentStream.showText("Recipe Name: " + meal.getRecipe().getName());
        contentStream.newLineAtOffset(0, -19);
        contentStream.showText("Ingredients: " + ingredients);
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Instructions: " + meal.getRecipe().getReadme());
        contentStream.newLineAtOffset(0, -21);
        contentStream.endText();
      }
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      document.save(baos);
      return baos.toByteArray();
    }
  }
}
