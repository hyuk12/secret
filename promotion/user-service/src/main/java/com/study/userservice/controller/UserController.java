package com.study.userservice.controller;

import com.study.userservice.dto.UserDto;
import com.study.userservice.entity.User;
import com.study.userservice.entity.UserLoginHistory;
import com.study.userservice.exception.DuplicateUserException;
import com.study.userservice.exception.UnauthorizedAccessException;
import com.study.userservice.exception.UserNotFoundException;
import com.study.userservice.service.UserService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/signup")
  public ResponseEntity<?> createUser(
      @RequestBody UserDto.SignupRequest request) {
    User user = userService.createUser(request.getEmail(), request.getPassword(), request.getName());
    return ResponseEntity.status(HttpStatus.CREATED).body(UserDto.Response.from(user));
  }

  @GetMapping("/me")
  public ResponseEntity<?> getProfile(
      @RequestHeader("X-USER-ID") Integer userId) {
    User user = userService.getUserById(userId);
    return ResponseEntity.ok(UserDto.Response.from(user));
  }

  @PutMapping("/me")
  public ResponseEntity<?> updateProfile(
      @RequestHeader("X-USER-ID") Integer userId,
      @RequestBody UserDto.UpdateRequest request) {
    User user = userService.updateUser(userId, request.getName());
    return ResponseEntity.ok(UserDto.Response.from(user));
  }

  @PostMapping("/me/password")
  public ResponseEntity<?> changePassword(
      @RequestHeader("X-USER-ID") Integer userId,
      @RequestBody UserDto.PasswordChangeRequest request) {
    userService.changePassword(userId, request.getCurrentPassword(), request.getNewPassword());
    return ResponseEntity.ok().build();
  }

  @GetMapping("/me/login-history")
  public ResponseEntity<List<UserLoginHistory>> getLoginHistory(
      @RequestHeader("X-USER-ID") Integer userId) {
    List<UserLoginHistory> history = userService.getUserLoginHistory(userId);
    return ResponseEntity.ok(history);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<String> handleUserNotFound(UserNotFoundException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
  }

  @ExceptionHandler(DuplicateUserException.class)
  public ResponseEntity<String> handleDuplicateUser(DuplicateUserException exception) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
  }

  @ExceptionHandler(UnauthorizedAccessException.class)
  public ResponseEntity<String> handleUnauthorizedAccess(UnauthorizedAccessException exception) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
  }
}
