package br.com.jr.cloudsystemfile.domain.usuario.controller;

import br.com.jr.cloudsystemfile.domain.usuario.data.record.AuthDTO;
import br.com.jr.cloudsystemfile.domain.usuario.data.record.TokenDTO;
import br.com.jr.cloudsystemfile.domain.usuario.data.record.UsuarioDTO;
import br.com.jr.cloudsystemfile.domain.usuario.data.record.UsuarioRegisterDTO;
import br.com.jr.cloudsystemfile.domain.usuario.service.UsuarioService;
import br.com.jr.cloudsystemfile.domain.usuario.config.security.AuthenticationManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    private final AuthenticationManagerService authenticationManagerService;


    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody AuthDTO authDTO) {
        TokenDTO token = authenticationManagerService.authenticate(authDTO.email(), authDTO.password());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioDTO> register(@RequestBody AuthDTO authDTO, UriComponentsBuilder uriBuilder) {

        UsuarioRegisterDTO usuarioDTO = new UsuarioRegisterDTO(authDTO.nome(), authDTO.email(), authDTO.password());

        UsuarioDTO novoUsuario = usuarioService.save(usuarioDTO);

        URI location = uriBuilder
                .path("/v1/usuarios/{id}")
                .buildAndExpand(novoUsuario.id())
                .toUri();

        return ResponseEntity.created(location).body(novoUsuario);

    }


}
