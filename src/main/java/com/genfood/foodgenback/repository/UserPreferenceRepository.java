package com.genfood.foodgenback.repository;

import com.genfood.foodgenback.repository.model.User;
import com.genfood.foodgenback.repository.model.UserPreference;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreference, String> {
  List<UserPreference> findAllByUser(User user);
}
