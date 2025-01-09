package br.com.jr.cloudsystemfile.infra.advice.handler;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PayloadMessageErrorHandler implements Serializable {

    private static final long serialVersionUID = 1L;

    private String payload;

    private String message;

}

