package com.libre.librerias.repositorios;

import com.libre.librerias.entidades.Autor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepo extends JpaRepository<Autor, String> {
    
    @Query("SELECT c FROM Autor c WHERE c.nombre = :idd")
    public Autor buscarPorName(@Param("idd") String nombre);
    
    @Query("SELECT c FROM Autor c order by c.alta DESC")
    public List<Autor> listarAutores();
    
}
