package com.genfood.foodgenback.security;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PUT;

import com.genfood.foodgenback.endpoint.rest.model.Role;
import com.genfood.foodgenback.service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor
public class SecurityConf {

  private final JWTFilter jwtFilter;
  private final UserDetailsServiceImpl userDetailsService;

  @Bean
  public UserDetailsService userDetailsService() {
    return userDetailsService;
  }

  @Bean
  public SecurityFilterChain filterChain(
      HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
    return http.cors(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(GET, "/ping")
                    .permitAll()
                    .requestMatchers("/users/signup")
                    .permitAll()
                    .requestMatchers("/users/login")
                    .permitAll()
                    .requestMatchers("/users/whoami")
                    .authenticated()
                    .requestMatchers(GET, "/users/**")
                    .hasAnyRole(String.valueOf(Role.ADMIN))
                    .requestMatchers(PUT, "/users/**")
                    .hasAnyRole(String.valueOf(Role.ADMIN))
                    .requestMatchers(GET, "/regions")
                    .authenticated()
                    .requestMatchers(PUT, "/regions/**")
                    .hasAnyRole(String.valueOf(Role.ADMIN))
                    .requestMatchers(GET, "/recipes/**")
                    .authenticated()
                    .requestMatchers(PUT, "/recipes/**")
                    .hasAnyRole(String.valueOf(Role.ADMIN))
                    .requestMatchers(GET, "/meals/**")
                    .authenticated()
                    .requestMatchers(PUT, "/meals/download/{id}")
                    .authenticated()
                    .requestMatchers(PUT, "/meals/**")
                    .hasAnyRole(String.valueOf(Role.ADMIN))
                    .requestMatchers(GET, "/allergy")
                    .authenticated()
                    .requestMatchers(PUT, "/allergy/**")
                    .hasAnyRole(String.valueOf(Role.ADMIN))
                    .requestMatchers(GET, "/ingredients/**")
                    .permitAll()
                    .requestMatchers(PUT, "/ingredients/**")
                    .hasAnyRole(String.valueOf(Role.ADMIN))
                    .anyRequest()
                    .authenticated())
        .authenticationManager(authenticationManager)
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsService());
    authenticationProvider.setPasswordEncoder(passwordEncoder());
    return authenticationProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }
}
