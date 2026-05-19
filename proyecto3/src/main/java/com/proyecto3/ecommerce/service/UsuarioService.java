package com.proyecto3.ecommerce.service;

import com.proyecto3.ecommerce.model.Rol;
import com.proyecto3.ecommerce.model.Usuario;
import com.proyecto3.ecommerce.repository.RolRepository;
import com.proyecto3.ecommerce.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UsuarioService {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private RolRepository rolRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    /**
     * Registrar nuevo cliente. Asigna ROLE_CLIENTE automaticamente.
     */
    public Usuario registrarCliente(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("El email ya esta registrado.");
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        Rol rolCliente = rolRepository.findByNombre("ROLE_CLIENTE")
            .orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado"));
        usuario.setRoles(Set.of(rolCliente));
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    public void toggleActivo(Long id) {
        usuarioRepository.findById(id).ifPresent(u -> {
            u.setActivo(!u.isActivo());
            usuarioRepository.save(u);
        });
    }
}
