package com.genfood.foodgenback.repository;

import com.genfood.foodgenback.repository.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, String> {
  boolean existsByName(String name);

  @Query(nativeQuery = true, value = "SELECT * from meal order by random() limit 3")
  List<Meal> findMealRandomly();
}
