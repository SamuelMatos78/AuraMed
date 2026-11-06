package com.auramed.auramed.controller.api;

import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.auramed.auramed.controller.api.ApiDtos.ApiError;

@RestControllerAdvice(basePackages = "com.auramed.auramed.controller.api")
public class ApiExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> requisicaoInvalida(IllegalArgumentException erro) {
        return ResponseEntity.badRequest().body(new ApiError(erro.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> jsonInvalido(HttpMessageNotReadableException erro) {
        return ResponseEntity.badRequest().body(new ApiError("Corpo JSON inválido ou incompleto"));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiError> naoEncontrado(NoSuchElementException erro) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError(erro.getMessage()));
    }

    @ExceptionHandler({IllegalStateException.class, DataIntegrityViolationException.class})
    public ResponseEntity<ApiError> conflito(RuntimeException erro) {
        String mensagem = erro instanceof IllegalStateException && erro.getMessage() != null
            ? erro.getMessage() : "O registro conflita com os dados existentes";
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiError(mensagem));
    }
}
