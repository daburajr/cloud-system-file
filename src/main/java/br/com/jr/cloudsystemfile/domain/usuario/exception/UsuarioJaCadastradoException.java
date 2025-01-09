package br.com.jr.cloudsystemfile.domain.usuario.exception;

import br.com.jr.cloudsystemfile.infra.exception.InvalidOperationException;

public class UsuarioJaCadastradoException extends InvalidOperationException {

    public UsuarioJaCadastradoException(String message) {
        super(message);
    }

}
