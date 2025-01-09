package br.com.jr.cloudsystemfile.infra.advice;


import br.com.jr.cloudsystemfile.infra.advice.handler.StandardErrorHandler;
import br.com.jr.cloudsystemfile.infra.advice.handler.ValidationErrorHandler;
import br.com.jr.cloudsystemfile.infra.exception.ObjectNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlerController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardErrorHandler> handlerValidacaoArgumentoInvalido(MethodArgumentNotValidException e,
                                                                                  HttpServletRequest request) {

        ValidationErrorHandler err = this.geraValidationErrorHandler(
                request);

        List<FieldError> fieldErrorList = e.getBindingResult().getFieldErrors();
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();

        if (!fieldErrorList.isEmpty()) {
            fieldErrorList.forEach(f -> err.addFieldMessageErrorHandler(f.getField(), f.getDefaultMessage()));
        } else {
            allErrors.forEach(f -> err.addPayloadMessageErrorHandler(f.getObjectName(), f.getDefaultMessage()));
        }

        log.error("MethodArgumentNotValidException", e);

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<StandardErrorHandler> handlerValidacaoConstraintViolada(ConstraintViolationException e,
                                                                                  HttpServletRequest request) {

        ValidationErrorHandler err = this.geraValidationErrorHandler(
                request);

        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();

        violations.forEach(f -> err.addFieldMessageErrorHandler(f.getPropertyPath().toString().split("\\.")[1],
                f.getMessageTemplate()));

        log.error("ConstraintViolationException", e);

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseEntity<StandardErrorHandler> hanlderValidacaoTipoInvalido(UnexpectedTypeException e, HttpServletRequest request) {
        StandardErrorHandler err = this.geraStandarErrorHandler(HttpStatus.UNPROCESSABLE_ENTITY,
                "Tipo passado Inesperado", request);
        log.error("UnexpectedTypeException", e);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<StandardErrorHandler> hanlderValidacaoMetodoInvalido(HttpRequestMethodNotSupportedException e,
                                                                               HttpServletRequest request) {
        StandardErrorHandler err = this.geraStandarErrorHandler(HttpStatus.METHOD_NOT_ALLOWED,
                "Método de requisição " + e.getMethod() + " não suportado [endpoint]", request);
        log.error("UnexpectedTypeException", e);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(err);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<StandardErrorHandler> handlerBadRequest(Exception e,
                                                                  HttpServletRequest request) {
        StandardErrorHandler err = this.geraStandarErrorHandler(
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                request);

        log.error("HandlerBadRequest", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardErrorHandler> handlerValidacaoObjectNotFound(ObjectNotFoundException e,
                                                                               HttpServletRequest request) {
        StandardErrorHandler err = this.geraStandarErrorHandler(
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                request);

        log.error("ObjectNotFoundException", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<StandardErrorHandler> handlerValidacaoAuthorizationDeniedException(AuthorizationDeniedException e,
                                                                                             HttpServletRequest request) {
        StandardErrorHandler err = this.geraStandarErrorHandler(
                HttpStatus.FORBIDDEN,
                e.getMessage(),
                request);

        log.error("AuthorizationDeniedException", e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<StandardErrorHandler> handlerValidacaoAuthenticationException(AuthenticationException e,
                                                                                        HttpServletRequest request) {
        StandardErrorHandler err = this.geraStandarErrorHandler(
                HttpStatus.FORBIDDEN,
                e.getMessage(),
                request);

        log.error("AuthenticationException", e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardErrorHandler> handlerAll(Exception e, HttpServletRequest request) {
        StandardErrorHandler err = this.geraStandarErrorHandler(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro Inesperado",
                request);

        log.error("Exception: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<StandardErrorHandler> handlerFileStorage(FileNotFoundException e,
                                                                   HttpServletRequest request) {
        StandardErrorHandler err = this.geraStandarErrorHandler(
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                request);

        log.error("FileStorageException: ", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    private ValidationErrorHandler geraValidationErrorHandler(HttpServletRequest request) {
        return new ValidationErrorHandler(
                LocalDateTime.now(),
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                HttpStatus.UNPROCESSABLE_ENTITY.name(),
                "Falha ao validar os campos do json",
                request.getServletPath());
    }

    private StandardErrorHandler geraStandarErrorHandler(HttpStatus httpStatus,
                                                         String message,
                                                         HttpServletRequest request) {
        return new StandardErrorHandler(
                LocalDateTime.now(),
                httpStatus.value(),
                httpStatus.name(),
                message,
                request.getServletPath());
    }


}
