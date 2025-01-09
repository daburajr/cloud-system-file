package br.com.jr.cloudsystemfile.domain.usuario.data.model;


import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    private BigDecimal id;

    private String name;

}
