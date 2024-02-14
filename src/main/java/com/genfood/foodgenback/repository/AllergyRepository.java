package com.genfood.foodgenback.repository;

import com.genfood.foodgenback.repository.model.Allergy;
import com.genfood.foodgenback.repository.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllergyRepository extends JpaRepository<Allergy, String> {
  List<Allergy> findAllByUser(User user);
}
