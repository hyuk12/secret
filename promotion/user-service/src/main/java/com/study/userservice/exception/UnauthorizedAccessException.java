package com.study.userservice.exception;

public class UnauthorizedAccessException extends RuntimeException{
  public UnauthorizedAccessException(String message) {
    super(message);
  }
}
