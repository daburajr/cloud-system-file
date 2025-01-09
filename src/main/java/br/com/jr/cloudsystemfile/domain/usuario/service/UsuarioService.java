package br.com.jr.cloudsystemfile.domain.usuario.service;


import br.com.jr.cloudsystemfile.domain.usuario.data.model.Usuario;
import br.com.jr.cloudsystemfile.domain.usuario.data.record.UsuarioDTO;
import br.com.jr.cloudsystemfile.domain.usuario.data.record.UsuarioRegisterDTO;
import br.com.jr.cloudsystemfile.domain.usuario.exception.UsuarioJaCadastradoException;
import br.com.jr.cloudsystemfile.infra.util.KeyGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;


@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final PasswordEncoder passwordEncoder;

    public Usuario buscaPorEmail(String email) {
        throw new UnsupportedOperationException("Feature incomplete");
    }

    public UsuarioDTO save(UsuarioRegisterDTO usuarioDTO) {

        Usuario usuario = buscaPorEmail(usuarioDTO.email());

        if (Objects.nonNull(usuario)) {
            throw new UsuarioJaCadastradoException("Usuario ja cadastrado");
        }

        String encryptedPassword = passwordEncoder.encode(usuarioDTO.password());

        Usuario newUsuario = Usuario.builder()
                .id(new BigDecimal(KeyGenerator.next()))
                .nome(usuarioDTO.nome())
                .email(usuarioDTO.email())
                .password(encryptedPassword)
                .build();

        // TODO salvar newUsuario na base de dados

        return new UsuarioDTO(newUsuario.getId(), newUsuario.getNome(), newUsuario.getEmail());

    }

}
