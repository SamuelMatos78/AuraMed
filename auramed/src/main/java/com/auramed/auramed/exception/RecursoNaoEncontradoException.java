package com.auramed.auramed.exception;

import java.util.NoSuchElementException;

public class RecursoNaoEncontradoException extends NoSuchElementException {
    public RecursoNaoEncontradoException(String mensagem) { super(mensagem); }
}
