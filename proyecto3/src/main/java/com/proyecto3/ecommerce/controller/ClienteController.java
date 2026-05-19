package com.proyecto3.ecommerce.controller;

import com.proyecto3.ecommerce.service.ProductoService;
import com.proyecto3.ecommerce.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cliente")
@PreAuthorize("hasAnyRole('CLIENTE','ADMIN')")
public class ClienteController {

    @Autowired private ProductoService productoService;
    @Autowired private UsuarioService usuarioService;

    @GetMapping("/home")
    public String home(Model model, Authentication auth) {
        model.addAttribute("productos", productoService.listarActivos());
        model.addAttribute("emailUsuario", auth.getName());
        usuarioService.findByEmail(auth.getName())
            .ifPresent(u -> model.addAttribute("usuario", u));
        return "cliente/home";
    }
}

// ---- Controlador publico del catalogo ----
// (en el mismo archivo para simplicidad)
class CatalogoController {
    // Ver PublicController.java
}
