package br.com.jr.cloudsystemfile.domain.usuario.service;


import br.com.jr.cloudsystemfile.domain.usuario.data.record.UsuarioRegisterDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UsuarioServiceTest {

    private AutoCloseable closeable;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void deve_realizar_busca_por_email() {
        assertThrows(UnsupportedOperationException.class,
                () -> usuarioService.buscaPorEmail("teste@example.com"),
                "Feature incomplete");
    }

    @Test
    void deve_lancar_excecao_quando_usuario_ja_cadastrado() {

        String email = "user@domain.com";
        UsuarioRegisterDTO usuarioRegisterDTO = new UsuarioRegisterDTO("Nome", email, "senha123");

        assertThrows(UnsupportedOperationException.class,
                () -> usuarioService.save(usuarioRegisterDTO),
                "Feature incomplete");
    }

}