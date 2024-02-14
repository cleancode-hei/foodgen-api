package com.genfood.foodgenback.service;

import com.genfood.foodgenback.repository.UserPreferenceRepository;
import com.genfood.foodgenback.repository.model.User;
import com.genfood.foodgenback.repository.model.UserPreference;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserPreferenceService {
  private final UserPreferenceRepository userPreferenceRepository;

  public List<UserPreference> getPreferencesByUser(User user) {
    return userPreferenceRepository.findAllByUser(user);
  }
}
