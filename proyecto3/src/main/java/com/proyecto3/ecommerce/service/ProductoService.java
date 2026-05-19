package com.proyecto3.ecommerce.service;

import com.proyecto3.ecommerce.model.Producto;
import com.proyecto3.ecommerce.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoService {

    @Autowired private ProductoRepository productoRepository;

    public List<Producto> listarActivos() {
        return productoRepository.findByActivoTrue();
    }

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }

    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    public void eliminar(Long id) {
        productoRepository.findById(id).ifPresent(p -> {
            p.setActivo(false); // Soft delete
            productoRepository.save(p);
        });
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCaseAndActivoTrue(nombre);
    }
}
