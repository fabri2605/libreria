package com.libre.librerias.repositorios;

import com.libre.librerias.entidades.Libro;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepo extends JpaRepository<Libro, String> {
    
    @Query("SELECT c FROM Libro c WHERE c.titulo = :idd")
    public Optional<Libro> buscarPorTitulo(@Param("idd") String titulo);
    
    @Query("SELECT c FROM Libro c order by c.alta DESC")
    public List<Libro> listarLibros();
    
}
