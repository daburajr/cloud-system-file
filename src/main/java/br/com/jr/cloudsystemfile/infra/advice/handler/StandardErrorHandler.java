package br.com.jr.cloudsystemfile.infra.advice.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StandardErrorHandler implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timeStamp;

    private Integer status;

    private String error;

    private String message;

    private String path;


}
