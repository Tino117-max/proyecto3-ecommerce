package com.proyecto3.ecommerce.controller;

import com.proyecto3.ecommerce.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PublicController {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/catalogo")
    public String catalogo(@RequestParam(required = false) String buscar, Model model) {
        if (buscar != null && !buscar.isEmpty()) {
            model.addAttribute("productos", productoService.buscarPorNombre(buscar));
            model.addAttribute("buscar", buscar);
        } else {
            model.addAttribute("productos", productoService.listarActivos());
        }
        return "public/catalogo";
    }
}
