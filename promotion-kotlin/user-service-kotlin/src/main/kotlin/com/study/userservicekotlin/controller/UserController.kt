package com.study.userservicekotlin.controller

import com.study.userservicekotlin.controller.dto.Response
import com.study.userservicekotlin.controller.dto.SignupRequest
import com.study.userservicekotlin.controller.dto.toResponse
import com.study.userservicekotlin.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v2/users")
class UserController(
    private val userService: UserService,
) {

    @PostMapping("/signup")
    fun createUser(
        @RequestBody request: SignupRequest,
    ): ResponseEntity<Response> {
        val user = userService.createUser(request.email, request.password, request.name)
        return ResponseEntity.status(HttpStatus.CREATED).body(user.toResponse())
    }
}
