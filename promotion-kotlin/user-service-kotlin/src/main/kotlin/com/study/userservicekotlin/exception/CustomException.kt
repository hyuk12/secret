package com.study.userservicekotlin.exception

import kotlin.RuntimeException

class DuplicateUserException(message: String) : RuntimeException(message) {
}


class UnauthorizedAccessException(message: String) : RuntimeException(message) {
}

class UserNotFoundException(message: String) : RuntimeException(message) {
}


