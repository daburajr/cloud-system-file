package br.com.jr.cloudsystemfile.infra.advice.handler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class ValidationErrorHandler extends StandardErrorHandler {

    private List<FieldMessageErrorHandler> fieldErrors;

    private List<PayloadMessageErrorHandler> payloadErros;

    public ValidationErrorHandler() {
        this.fieldErrors = new ArrayList<>();
    }

    public ValidationErrorHandler(LocalDateTime timeStamp, Integer status, String error, String message, String path) {
        super(timeStamp, status, error, message, path);
        this.fieldErrors = new ArrayList<>();
        this.payloadErros = new ArrayList<>();
    }

    public void setFieldErrors(List<FieldMessageErrorHandler> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public List<FieldMessageErrorHandler> getFieldErrors() {
        return fieldErrors;
    }

    public List<PayloadMessageErrorHandler> getPayloadErros() {
        return payloadErros;
    }

    public void setPayloadErros(List<PayloadMessageErrorHandler> payloadErros) {
        this.payloadErros = payloadErros;
    }

    public void addFieldMessageErrorHandler(String fieldName, String message) {
        fieldErrors.add(FieldMessageErrorHandler.builder()
                .fieldName(fieldName)
                .message(message)
                .build());
    }

    public void addPayloadMessageErrorHandler(String payload, String message) {
        payloadErros.add(PayloadMessageErrorHandler.builder()
                .payload(payload)
                .message(message)
                .build());
    }


}
