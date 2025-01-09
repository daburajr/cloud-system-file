package br.com.jr.cloudsystemfile.infra.advice.handler;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FieldMessageErrorHandler implements Serializable {

    private static final long serialVersionUID = 1L;

    private String fieldName;

    private String message;

}

