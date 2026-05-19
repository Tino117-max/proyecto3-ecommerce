package com.proyecto3.ecommerce.service;

import com.proyecto3.ecommerce.model.Usuario;
import com.proyecto3.ecommerce.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Servicio que Spring Security usa para cargar el usuario desde la BD.
 * Es el puente entre el sistema de autenticacion y nuestra entidad Usuario.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Spring Security llama este metodo al hacer login.
     * Buscamos el usuario por email y construimos el UserDetails.
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException(
                "Usuario no encontrado con email: " + email));

        if (!usuario.isActivo()) {
            throw new UsernameNotFoundException("La cuenta esta desactivada.");
        }

        // Convertir roles a GrantedAuthority para Spring Security
        Set<GrantedAuthority> authorities = usuario.getRoles().stream()
            .map(rol -> new SimpleGrantedAuthority(rol.getNombre()))
            .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
            usuario.getEmail(),
            usuario.getPassword(),
            usuario.isActivo(),
            true, true, true,
            authorities
        );
    }
}
