package com.study.userservicekotlin.service

import com.study.userservicekotlin.entity.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.util.*

@Service
@Slf4j
class JwtService(
    private val passwordEncoder: PasswordEncoder
) {

    @Value("\${jwt.secret}")
    private lateinit var secret: String

    fun generateToken(user: User): String {
        val currentTimeMillis = System.currentTimeMillis()
        return Jwts.builder()
            .subject(user.email)
            .claim("role", "USER")
            .issuedAt(Date(currentTimeMillis))
            .expiration(Date(currentTimeMillis + 3600000))
            .signWith(Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8)))
            .compact()
    }

    fun validateToken(token: String): Claims {
        try {
            return parseJwtClaims(token)
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid JWT token")
        }
    }

    fun parseJwtClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8)))
            .build()
            .parseSignedClaims(token)
            .payload
    }

    fun refreshToken(token: String): String {
        val parseJwtClaims = parseJwtClaims(token)
        val currentTimeMillis = System.currentTimeMillis()
        return Jwts.builder()
            .subject(parseJwtClaims.subject)
            .claims(parseJwtClaims)
            .issuedAt(Date(currentTimeMillis))
            .expiration(Date(currentTimeMillis + 3600000))
            .signWith(Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8)))
            .compact()
    }
}
