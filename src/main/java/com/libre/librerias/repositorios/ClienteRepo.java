

package com.libre.librerias.repositorios;

import com.libre.librerias.entidades.Cliente;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepo extends JpaRepository<Cliente, String> {
    
    @Query("SELECT c FROM Cliente c order by c.alta DESC")
    public List<Cliente> listarClientes();
    
    @Query("SELECT c FROM Cliente c WHERE c.nombre = :nameee")
    public Optional<Cliente> buscarPorNombre(@Param("nameee") String nombre);
    
}