package com.genfood.foodgenback.service;

import com.genfood.foodgenback.endpoint.rest.model.Principal;
import com.genfood.foodgenback.repository.UserRepository;
import com.genfood.foodgenback.repository.model.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user =
        userRepository
            .findByEmail(username)
            .orElseThrow(
                () -> new UsernameNotFoundException("User of email: " + username + " not found"));
    return Principal.builder().user(user).build();
  }
}
