package com.proyecto3.ecommerce.controller;

import com.proyecto3.ecommerce.model.Producto;
import com.proyecto3.ecommerce.service.ProductoService;
import com.proyecto3.ecommerce.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired private ProductoService productoService;
    @Autowired private UsuarioService usuarioService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalProductos", productoService.listarTodos().size());
        model.addAttribute("totalUsuarios", usuarioService.listarTodos().size());
        return "admin/dashboard";
    }

    // ----- PRODUCTOS -----
    @GetMapping("/productos")
    public String listarProductos(Model model) {
        model.addAttribute("productos", productoService.listarTodos());
        return "admin/productos";
    }

    @GetMapping("/productos/nuevo")
    public String nuevoProductoForm(Model model) {
        model.addAttribute("producto", new Producto());
        return "admin/producto-form";
    }

    @PostMapping("/productos/guardar")
    public String guardarProducto(@Valid @ModelAttribute Producto producto,
                                  BindingResult result,
                                  RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) return "admin/producto-form";
        productoService.guardar(producto);
        redirectAttrs.addFlashAttribute("successMsg", "Producto guardado correctamente.");
        return "redirect:/admin/productos";
    }

    @GetMapping("/productos/editar/{id}")
    public String editarProducto(@PathVariable Long id, Model model) {
        Producto p = productoService.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        model.addAttribute("producto", p);
        return "admin/producto-form";
    }

    @GetMapping("/productos/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        productoService.eliminar(id);
        redirectAttrs.addFlashAttribute("successMsg", "Producto eliminado.");
        return "redirect:/admin/productos";
    }

    // ----- USUARIOS -----
    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "admin/usuarios";
    }
}
