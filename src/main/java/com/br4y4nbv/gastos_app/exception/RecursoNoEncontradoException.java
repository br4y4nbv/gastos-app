package com.br4y4nbv.gastos_app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// @ResponseStatus hace que Spring responda con HTTP 404 cuando se lanza esta excepción.
// Spring Boot busca entonces la plantilla templates/error/404.html automáticamente.
@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}