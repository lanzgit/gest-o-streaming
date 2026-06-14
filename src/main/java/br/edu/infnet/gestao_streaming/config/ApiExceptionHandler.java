package br.edu.infnet.gestao_streaming.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class ApiExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	ProblemDetail handleInvalidRequest(IllegalArgumentException exception) {
		ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
		problem.setTitle("Invalid request");
		problem.setDetail(exception.getMessage());
		return problem;
	}
}
