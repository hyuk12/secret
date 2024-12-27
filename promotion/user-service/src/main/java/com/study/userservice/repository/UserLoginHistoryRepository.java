package com.study.userservice.repository;

import com.study.userservice.entity.User;
import com.study.userservice.entity.UserLoginHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLoginHistoryRepository extends JpaRepository<UserLoginHistory, Long> {
  List<UserLoginHistory> findByUserOrderByLoginTimeDesc(User user);
}
