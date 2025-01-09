package br.com.jr.cloudsystemfile.domain.usuario.exception;

import br.com.jr.cloudsystemfile.infra.exception.ObjectNotFoundException;

public class UsuarioNaoEncontradoException extends ObjectNotFoundException {
    public UsuarioNaoEncontradoException(String message) {
        super(message);
    }
}
