package com.study.userservicekotlin.controller

import com.study.userservicekotlin.controller.dto.LoginRequest
import com.study.userservicekotlin.controller.dto.LoginResponse
import com.study.userservicekotlin.controller.dto.TokenRequest
import com.study.userservicekotlin.controller.dto.TokenResponse
import com.study.userservicekotlin.controller.dto.toResponse
import com.study.userservicekotlin.service.JwtService
import com.study.userservicekotlin.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v2/users")
class AuthController(
    private val jwtService: JwtService,
    private val userService: UserService,
) {

    @PostMapping("/login")
    fun login (
        @RequestBody loginRequest: LoginRequest,
    ): ResponseEntity<*> {
        val user = userService.authenticate(loginRequest.email, loginRequest.password)
        val generateToken = jwtService.generateToken(user)
        return ResponseEntity.ok(LoginResponse(generateToken, user.toResponse()))
    }

    @PostMapping("/validate-token")
    fun validateToken(
        @RequestBody request: TokenRequest,
    ): ResponseEntity<*> {
        val claims = jwtService.validateToken(request.token)
        return ResponseEntity.ok(TokenResponse(claims.subject, true, claims.get("role", String::class.java)))
    }
}
