package com.libre.librerias.repositorios;

import com.libre.librerias.entidades.Editorial;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EditorialRepo extends JpaRepository<Editorial, String> {
    
    @Query("SELECT p FROM Editorial p WHERE p.nombre = :idd")
    public Editorial buscarPorName(@Param("idd") String nombre);
    
    @Query("SELECT p FROM Editorial p order by p.alta DESC")
    public List<Editorial> listarEditoriales();
    
    @Query("SELECT p FROM Editorial p WHERE p.alta = true")
    public List<Editorial> listarEditorialesA();
    

}