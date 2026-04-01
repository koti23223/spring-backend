package com.smartcart.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(EmailAlreadyExistsException.class)
	public ResponseEntity<ApiErrorResponse> handleEmailExists(EmailAlreadyExistsException ex) {

		return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
	}

	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<ApiErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex) {

		return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
	}

	@ExceptionHandler(PasswordMismatchException.class)
	public ResponseEntity<ApiErrorResponse> handlePasswordMismatch(PasswordMismatchException ex) {

		return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ApiErrorResponse> handleUserNotFound(UserNotFoundException ex) {

		return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
	}

	private ResponseEntity<ApiErrorResponse> buildResponse(HttpStatus status, String message) {

		ApiErrorResponse error = ApiErrorResponse.builder().status(status.value()).error(status.getReasonPhrase())
				.message(message).timestamp(LocalDateTime.now()).build();

		return new ResponseEntity<>(error, status);
	}

}
