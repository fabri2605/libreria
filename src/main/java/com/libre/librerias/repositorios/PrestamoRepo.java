

package com.libre.librerias.repositorios;

import com.libre.librerias.entidades.Cliente;
import com.libre.librerias.entidades.Prestamo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamoRepo extends JpaRepository<Prestamo, String> {
    
    @Query("SELECT a FROM Prestamo a order by a.alta DESC")
    public List<Prestamo> listarPrestamos();
    @Query("SELECT a FROM Prestamo a WHERE a.id = :idd")
    public Optional<Prestamo> buscar(@Param ("idd") String id) ;
}