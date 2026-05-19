package com.proyecto3.ecommerce.controller;

import com.proyecto3.ecommerce.model.Usuario;
import com.proyecto3.ecommerce.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    /** GET /login - Muestra el formulario de login */
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        if (error != null) {
            model.addAttribute("errorMsg", "Credenciales incorrectas. Verifique su email y contrasena.");
        }
        if (logout != null) {
            model.addAttribute("logoutMsg", "Sesion cerrada correctamente.");
        }
        return "auth/login";
    }

    /** GET /registro - Muestra el formulario de registro */
    @GetMapping("/registro")
    public String registroPage(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "auth/registro";
    }

    /** POST /registro - Procesa el registro de nuevo usuario */
    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute("usuario") Usuario usuario,
                            BindingResult result,
                            @RequestParam("confirmPassword") String confirmPassword,
                            RedirectAttributes redirectAttrs,
                            Model model) {
        // Validar contrasenas coinciden
        if (!usuario.getPassword().equals(confirmPassword)) {
            model.addAttribute("errorMsg", "Las contrasenas no coinciden.");
            return "auth/registro";
        }

        if (result.hasErrors()) {
            return "auth/registro";
        }

        try {
            usuarioService.registrarCliente(usuario);
            redirectAttrs.addFlashAttribute("successMsg", "Cuenta creada exitosamente. Inicia sesion.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            model.addAttribute("errorMsg", e.getMessage());
            return "auth/registro";
        }
    }

    /** GET / - Pagina principal publica */
    @GetMapping("/")
    public String index() {
        return "redirect:/catalogo";
    }
}
