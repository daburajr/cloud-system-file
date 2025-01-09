package br.com.jr.cloudsystemfile.domain.usuario.data.record;

import java.math.BigDecimal;

public record RoleDTO(BigDecimal id, String nome) {

    public RoleDTO(String nome) {
        this(BigDecimal.ZERO, nome);
    }

}
