package com.genfood.foodgenback.repository;

import com.genfood.foodgenback.repository.model.Meal;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MealRepository extends JpaRepository<Meal, String> {
  boolean existsByName(String name);

  @Query(nativeQuery = true, value = "SELECT * from meal order by random() limit 1")
  Meal findMealRandomly();

  @Query(
      nativeQuery = true,
      value =
          "select * from (select m.* from meal m join recipe r on m.recipe_id = r.id join"
              + " recipe_ingredients ri on r.id = ri.recipe_id join user_preferences up on"
              + " ri.ingredient_id = up.ingredient_id where up.user_id = :userId group by m.id"
              + " having count(distinct up.ingredient_id) = (select count(*) from user_preferences"
              + " where user_id = :userId)) as subquery order by random()")
  List<Meal> findMealsByPreferences(@Param("userId") String userId, Pageable pageable);

  @Query(nativeQuery = true, value = "SELECT * from meal order by download desc")
  List<Meal> findAllOrderByDownload(Pageable pageable);

  @Query(
      nativeQuery = true,
      value =
          "select * from( select distinct m.* from meal m left join recipe r on m.recipe_id = r.id"
              + " left join recipe_ingredients ri on m.recipe_id = ri.recipe_id where"
              + " ri.ingredient_id not in(select ingredient_id from allergy where user_id ="
              + " :userId)) as subquery order by random() limit 3")
  List<Meal> findMealsWithoutAllergies(@Param("userId") String userId);
}
