package br.com.jr.cloudsystemfile.domain.usuario.config.security;



import br.com.jr.cloudsystemfile.domain.usuario.data.record.TokenDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationManagerService {

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    public TokenDTO authenticate(String email, String password) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(email, password);
        var auth = authenticationManager.authenticate(usernamePassword);

        String token = tokenService.generateToken((User) auth.getPrincipal());
        return new TokenDTO("bearer", token);
    }

}
