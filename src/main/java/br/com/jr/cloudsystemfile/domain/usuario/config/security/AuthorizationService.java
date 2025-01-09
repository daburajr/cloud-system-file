package br.com.jr.cloudsystemfile.domain.usuario.config.security;


import br.com.jr.cloudsystemfile.domain.usuario.data.model.Role;
import br.com.jr.cloudsystemfile.domain.usuario.data.model.Usuario;
import br.com.jr.cloudsystemfile.infra.util.KeyGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorizationService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario usuario = Usuario.builder()
                .id(BigDecimal.valueOf(KeyGenerator.next()))
                .nome("admin")
                .email("admin@admin")
                .password("$2a$10$uMpg6HqqhISm23nntVfS4uw4hjDMvYF7z64lSge27xJBUIEQD1c8y")
                .roles(List.of(new Role(BigDecimal.valueOf(1), "ADMIN"), new Role(BigDecimal.valueOf(2), "USER")))
                .build();

        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(),
                usuario.getPassword(),
                usuario.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName())).toList()
        );

    }
}
