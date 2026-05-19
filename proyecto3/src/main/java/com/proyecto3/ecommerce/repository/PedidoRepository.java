package com.proyecto3.ecommerce.repository;

import com.proyecto3.ecommerce.model.Pedido;
import com.proyecto3.ecommerce.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuarioOrderByFechaDesc(Usuario usuario);
}
