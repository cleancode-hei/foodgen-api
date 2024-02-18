package com.genfood.foodgenback.repository.validator;

import com.genfood.foodgenback.repository.UserRepository;
import com.genfood.foodgenback.repository.model.User;
import com.genfood.foodgenback.repository.model.exception.BadRequestException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MailValidator implements Consumer<User> {
  private final UserRepository userRepository;

  public void accept(List<User> users) {
    users.forEach(this::accept);
  }

  @Override
  public void accept(User user) {
    boolean alreadyExist = false;
    if (userRepository.existsByEmail(user.getEmail())
        && user.getId().compareTo(userRepository.findByEmail(user.getEmail()).get().getId()) == 1) {
      alreadyExist = true;
    }
    Set<String> violationMessages = new HashSet<>();
    if (alreadyExist) {
      violationMessages.add("Mail address already taken,try other");
    }
    if (!violationMessages.isEmpty()) {
      String formattedViolationMessages =
          violationMessages.stream().map(String::toString).collect(Collectors.joining(""));
      throw new BadRequestException(formattedViolationMessages);
    }
  }
}
