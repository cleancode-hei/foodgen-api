package com.genfood.foodgenback.endpoint.controller;

import com.genfood.foodgenback.endpoint.rest.mapper.UserMapper;
import com.genfood.foodgenback.endpoint.rest.model.Auth;
import com.genfood.foodgenback.endpoint.rest.model.SignUp;
import com.genfood.foodgenback.endpoint.rest.model.User;
import com.genfood.foodgenback.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
  private final UserMapper mapper;
  private final UserService service;

  @PostMapping("/signup")
  public String signUp(@RequestBody SignUp auth) {
    return service.signUp(auth);
  }

  @PostMapping("/login")
  public String signIn(@RequestBody Auth toAuthenticate) {
    return service.signIn(toAuthenticate);
  }

  @PutMapping
  public List<User> crupdateUsers(@RequestBody List<User> toCrupdate) {
    List<com.genfood.foodgenback.repository.model.User> entities =
        toCrupdate.stream().map(mapper::toEntity).collect(Collectors.toUnmodifiableList());
    return service.crupdateUsers(entities).stream()
        .map(mapper::toDto)
        .collect(Collectors.toUnmodifiableList());
  }

  @GetMapping("/{username}")
  public User getByUserName(@PathVariable String username) {
    return mapper.toDto(service.getUserByUsername(username));
  }

  @GetMapping("/whoami")
  public User whoami(HttpServletRequest request) {
    return mapper.toDto(service.whoami(request));
  }
}
