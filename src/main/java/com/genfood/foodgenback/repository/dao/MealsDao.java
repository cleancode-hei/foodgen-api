package com.genfood.foodgenback.repository.dao;

import com.genfood.foodgenback.repository.model.Meal;
import com.genfood.foodgenback.repository.model.Region;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class MealsDao {
  private EntityManager entityManager;

  public List<Meal> findByCriteria(Region region) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Meal> query = builder.createQuery(Meal.class);
    Root<Meal> root = query.from(Meal.class);
    Predicate predicate = builder.conjunction();

    Predicate hasRegion = builder.equal(root.get("region"), region);

    if (region != null) {
      predicate = builder.and(predicate, hasRegion);
    }

    query.where(predicate);

    return entityManager.createQuery(query).getResultList();
  }
}
