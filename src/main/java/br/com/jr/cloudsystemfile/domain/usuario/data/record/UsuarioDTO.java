package br.com.jr.cloudsystemfile.domain.usuario.data.record;

import java.math.BigDecimal;

public record UsuarioDTO(BigDecimal id, String nome, String email) {

    public UsuarioDTO(String nome, String email) {
        this(BigDecimal.ZERO, nome, email);
    }

}
