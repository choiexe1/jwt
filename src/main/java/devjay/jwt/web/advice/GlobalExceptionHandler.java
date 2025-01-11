package devjay.jwt.web.advice;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JWTVerificationException.class)
    public String jwtException(JWTVerificationException e) {
        return e.getMessage();
    }
}
