package com.genfood.foodgenback.repository;

import com.genfood.foodgenback.repository.model.Meal;
import com.genfood.foodgenback.repository.model.Recipe;
import com.genfood.foodgenback.repository.model.Region;
import com.genfood.foodgenback.repository.model.exception.NotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JDBCQueries {

  private final JdbcTemplate jdbcTemplate;
  private final RecipeRepository recipeRepository;
  private final RegionRepository regionRepository;

  @Autowired
  public JDBCQueries(
      DataSource dataSource, RecipeRepository recipeRepository, RegionRepository regionRepository) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.recipeRepository = recipeRepository;
    this.regionRepository = regionRepository;
  }

  public List<Meal> findAllMealsByCriterias(
      String regionName, List<String> allergies, List<String> ingredients) {

    List<Meal> meals = new ArrayList<>();

    StringBuilder sqlBuilder =
        new StringBuilder(
            "SELECT DISTINCT m.* FROM meal m "
                + "INNER JOIN region rg ON m.region_id = rg.id "
                + "INNER JOIN recipe r ON m.recipe_id = r.id "
                + "INNER JOIN recipe_ingredients ri ON r.id = ri.recipe_id "
                + "INNER JOIN ingredient i ON ri.ingredient_id = i.id ");

    List<Object> params = new ArrayList<>();

    if (regionName != null) {
      sqlBuilder.append(" WHERE rg.name = ?");
      params.add(regionName);
    }

    if (allergies != null && !allergies.isEmpty()) {
      if (params.isEmpty()) {
        sqlBuilder.append(" WHERE");
      } else {
        sqlBuilder.append(" AND");
      }
      sqlBuilder.append(
          " NOT EXISTS (SELECT * FROM recipe_ingredients ri2 INNER JOIN ingredient i2 ON"
              + " ri2.ingredient_id = i2.id WHERE ri2.recipe_id = m.recipe_id AND i2.name IN (");
      addParamsToSql(sqlBuilder, allergies.size());
      params.addAll(allergies);
      sqlBuilder.append("))");
    }

    if (ingredients != null && !ingredients.isEmpty()) {
      if (params.isEmpty()) {
        sqlBuilder.append(" WHERE");
      } else {
        sqlBuilder.append(" AND");
      }
      sqlBuilder.append(" i.name IN (");
      addParamsToSql(sqlBuilder, ingredients.size());
      params.addAll(ingredients);
      sqlBuilder.append(")");
    }

    try (Connection connection =
        Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
      PreparedStatement statement = connection.prepareStatement(sqlBuilder.toString());

      for (int i = 0; i < params.size(); i++) {
        statement.setObject(i + 1, params.get(i));
      }

      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        Meal meal = new Meal();
        meal.setId(resultSet.getString("id"));
        meal.setName(resultSet.getString("name"));

        String recipeId = resultSet.getString("recipe_id");
        Recipe recipe =
            recipeRepository
                .findById(recipeId)
                .orElseThrow(
                    () -> new NotFoundException("Recipe of id: " + recipeId + " not found."));

        meal.setRecipe(recipe);

        String regionId = resultSet.getString("region_id");
        Region region =
            regionRepository
                .findById(regionId)
                .orElseThrow(
                    () -> new NotFoundException("Region of id: " + recipeId + " not found."));
        meal.setRegion(region);

        meal.setImage(resultSet.getString("image"));
        meal.setDownload(resultSet.getInt("download"));

        meals.add(meal);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return meals;
  }

  private void addParamsToSql(StringBuilder sqlBuilder, int paramCount) {
    for (int i = 0; i < paramCount; i++) {
      sqlBuilder.append("?");
      if (i < paramCount - 1) {
        sqlBuilder.append(",");
      }
    }
  }
}
