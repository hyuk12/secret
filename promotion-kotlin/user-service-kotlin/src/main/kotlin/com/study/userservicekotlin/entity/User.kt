package com.study.userservicekotlin.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class User(
    name: String,
    email: String,
    passwordHash: String,

    ): BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long = 0

    @Column(name = "name", nullable = false, length = 100)
    var name: String = name
        protected set

    @Column(name = "email", nullable = false, unique = true, length = 100)
    var email: String = email
        protected set

    @Column(name= "password_hash", nullable = false, length = 255)
    @JsonIgnore
    var passwordHash: String = passwordHash
        protected set

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [(CascadeType.ALL)])
    @JsonBackReference
    var users: MutableList<UserLoginHistory> = mutableListOf()

}
