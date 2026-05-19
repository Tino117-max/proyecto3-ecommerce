package com.proyecto3.ecommerce.repository;

import com.proyecto3.ecommerce.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByActivoTrue();
    List<Producto> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);
}
