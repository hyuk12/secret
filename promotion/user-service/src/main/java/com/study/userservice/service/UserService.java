package com.study.userservice.service;

import com.study.userservice.entity.User;
import com.study.userservice.entity.UserLoginHistory;
import com.study.userservice.exception.DuplicateUserException;
import com.study.userservice.exception.UnauthorizedAccessException;
import com.study.userservice.exception.UserNotFoundException;
import com.study.userservice.repository.UserLoginHistoryRepository;
import com.study.userservice.repository.UserRepository;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final UserLoginHistoryRepository userLoginHistoryRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository,
      UserLoginHistoryRepository userLoginHistoryRepository,
      PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.userLoginHistoryRepository = userLoginHistoryRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  public User createUser(String email, String password, String name) {
    if (userRepository.findByEmail(email).isPresent()) {
      throw new DuplicateUserException("User already exists with email: " + email);
    }

    User user = new User();
    user.setEmail(email);
    user.setPasswordHash(passwordEncoder.encode(password));
    user.setName(name);

    return userRepository.save(user);
  }

  public User authenticate(String email, String password) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

    if (!passwordEncoder.matches(password, user.getPasswordHash())) {
      throw new UnauthorizedAccessException("Invalid credentials");
    }

    return user;
  }

  public User getUserById(Integer userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
  }

  @Transactional
  public User updateUser(Integer userId, String name) {
    User user = getUserById(userId);
    user.setName(name);
    return userRepository.save(user);
  }

  @Transactional
  public void changePassword(Integer userId, String currentPassword, String newPassword) {
    User user = getUserById(userId);

    if (!passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
      throw new UnauthorizedAccessException("Current password is incorrect");
    }

    user.setPasswordHash(passwordEncoder.encode(newPassword));
    userRepository.save(user);
  }

  public List<UserLoginHistory> getUserLoginHistory(Integer userId) {
    User user = getUserById(userId);
    return userLoginHistoryRepository.findByUserOrderByLoginTimeDesc(user);
  }
}
