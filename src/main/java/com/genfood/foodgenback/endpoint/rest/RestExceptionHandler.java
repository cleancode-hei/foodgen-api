package com.genfood.foodgenback.endpoint.rest;

import com.genfood.foodgenback.endpoint.rest.model.Exception;
import com.genfood.foodgenback.repository.model.exception.BadRequestException;
import com.genfood.foodgenback.repository.model.exception.ForbiddenException;
import com.genfood.foodgenback.repository.model.exception.NotFoundException;
import io.jsonwebtoken.JwtException;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

  @ExceptionHandler(
      value = {BadRequestException.class, DuplicateKeyException.class, JwtException.class})
  ResponseEntity<Exception> handleBadRequest(java.lang.Exception e) {
    log.info(e.getClass().getSimpleName(), e);
    Exception restException =
        Exception.builder()
            .type(BadRequestException.class.getSimpleName())
            .message(e.getMessage())
            .build();
    return new ResponseEntity<>(restException, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(
      value = {
        AccessDeniedException.class,
        ForbiddenException.class,
        BadCredentialsException.class
      })
  ResponseEntity<Exception> handleForbidden(java.lang.Exception e) {
    log.info(e.getClass().getSimpleName(), e);
    Exception restException =
        Exception.builder()
            .type(ForbiddenException.class.getSimpleName())
            .message(e.getMessage())
            .build();
    return new ResponseEntity<>(restException, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(value = {NotFoundException.class, UsernameNotFoundException.class})
  ResponseEntity<Exception> handleNotFound(java.lang.Exception e) {
    log.info(e.getClass().getSimpleName(), e);
    Exception restException =
        Exception.builder()
            .type(NotFoundException.class.getSimpleName())
            .message(e.getMessage())
            .build();
    return new ResponseEntity<>(restException, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = {java.lang.Exception.class, SQLException.class})
  ResponseEntity<Exception> handleDefault(java.lang.Exception e) {
    log.error("Internal error", e);
    Exception restException =
        Exception.builder()
            .type(HttpServerErrorException.InternalServerError.class.getSimpleName())
            .message(e.getMessage())
            .build();
    return new ResponseEntity<>(restException, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
