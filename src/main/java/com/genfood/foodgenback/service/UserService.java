package com.genfood.foodgenback.service;

import com.genfood.foodgenback.endpoint.rest.mapper.AuthMapper;
import com.genfood.foodgenback.endpoint.rest.model.Auth;
import com.genfood.foodgenback.endpoint.rest.model.Principal;
import com.genfood.foodgenback.endpoint.rest.model.Role;
import com.genfood.foodgenback.endpoint.rest.model.SignUp;
import com.genfood.foodgenback.repository.AllergyRepository;
import com.genfood.foodgenback.repository.UserPreferenceRepository;
import com.genfood.foodgenback.repository.UserRepository;
import com.genfood.foodgenback.repository.model.Allergy;
import com.genfood.foodgenback.repository.model.User;
import com.genfood.foodgenback.repository.model.UserPreference;
import com.genfood.foodgenback.repository.model.exception.NotFoundException;
import com.genfood.foodgenback.repository.validator.MailValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@Slf4j
public class UserService {
  private final UserRepository repository;
  private final MailValidator mailValidator;
  private final AllergyRepository allergyRepository;
  private final AuthMapper authMapper;
  private final UserPreferenceRepository userPreferenceRepository;
  private final UserDetailsServiceImpl userDetailsServiceImpl;
  private final JWTService jwtService;
  private final PasswordEncoder passwordEncoder;
  private final String AUTHORIZATION_HEADER = "Authorization";
  private final int BEARER_PREFIX_COUNT = 7;

  public User getUserByUsername(String userName) {
    return repository.findByUsername(userName);
  }

  public List<User> crupdateUsers(List<User> toCrupdate) {
    mailValidator.accept(toCrupdate);
    return repository.saveAll(toCrupdate);
  }

  public User getUserById(String id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("User of id: " + id + " does not exists"));
  }

  public User getUserByEmail(String email) {
    return repository
        .findByEmail(email)
        .orElseThrow(() -> new NotFoundException("User of email: " + email + " not found"));
  }

  public String signIn(Auth toAuthenticate) {
    String email = toAuthenticate.getEmail();
    UserDetails principal = userDetailsServiceImpl.loadUserByUsername(email);
    if (!passwordEncoder.matches(toAuthenticate.getPassword(), principal.getPassword())) {
      throw new BadCredentialsException("Wrong Password!");
    }
    return jwtService.generateToken(principal);
  }

  @Transactional
  public String signUp(SignUp user) {
    String email = user.getEmail();
    Optional<User> existingUser = repository.findByEmail(email);
    if (existingUser.isPresent()) {
      throw new DuplicateKeyException("User with the email address: " + email + " already exists.");
    }
    String hashedPassword = passwordEncoder.encode(user.getPassword());
    User createdUser =
        crupdateUsers(
                List.of(
                    User.builder()
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .role(Role.USER)
                        .password(hashedPassword)
                        .build()))
            .get(0);

    List<String> userPreferencesNames = user.getPreferences();
    List<String> allergiesNames = user.getAllergies();
    if (Objects.nonNull(createdUser)) {
      if (Objects.nonNull(userPreferencesNames)) {
        List<UserPreference> userPreferencesToCreate =
            userPreferencesNames.stream()
                .map((allergy) -> authMapper.userPreferenceToCreatableEntity(allergy, createdUser))
                .collect(Collectors.toUnmodifiableList());
        userPreferenceRepository.saveAll(userPreferencesToCreate);
      }
      if (Objects.nonNull(allergiesNames)) {
        List<Allergy> allergiesToCreate =
            allergiesNames.stream()
                .map((allergy) -> authMapper.allergyToCreatableEntity(allergy, createdUser))
                .collect(Collectors.toUnmodifiableList());

        allergyRepository.saveAll(allergiesToCreate);
      }
    }
    Principal principal = Principal.builder().user(createdUser).build();
    return jwtService.generateToken(principal);
  }

  public User whoami(HttpServletRequest request) {
    String authHeader = request.getHeader(AUTHORIZATION_HEADER);
    String token = authHeader.substring(BEARER_PREFIX_COUNT);
    String email = jwtService.extractEmail(token);
    return getUserByEmail(email);
  }
}
