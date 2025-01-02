package com.study.userservicekotlin.service

import com.study.userservicekotlin.entity.User
import com.study.userservicekotlin.exception.DuplicateUserException
import com.study.userservicekotlin.exception.UnauthorizedAccessException
import com.study.userservicekotlin.exception.UserNotFoundException
import com.study.userservicekotlin.repository.UserLoginHistoryRepository
import com.study.userservicekotlin.repository.UserRepository
import lombok.extern.slf4j.Slf4j
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userLoginHistoryRepository: UserLoginHistoryRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    @Transactional
    fun createUser(email: String, password: String, name: String): User {
        if (userRepository.findByEmail(email).isPresent) throw DuplicateUserException("User already exists: " + email)
        val user = User(name, email, passwordEncoder.encode(password))
        return userRepository.save(user)
    }

    fun authenticate(email: String, password: String): User {
        val user = userRepository.findByEmail(email)
            .orElseThrow()

        if (!passwordEncoder.matches(password, user.passwordHash)) throw UnauthorizedAccessException("Invalid password")
        return user
    }
}
