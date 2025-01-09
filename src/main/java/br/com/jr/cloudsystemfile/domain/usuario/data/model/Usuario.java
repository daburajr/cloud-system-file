package br.com.jr.cloudsystemfile.domain.usuario.data.model;


import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    private BigDecimal id;

    private int empresaId;

    private String nome;

    private String email;

    private String password;

    private List<Role> roles;

}
