

package com.libre.librerias.repositorios;

import com.libre.librerias.entidades.AppUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UserRepo extends JpaRepository<AppUser, String> {
    
    @Query("SELECT c FROM AppUser c")
    public List<AppUser> listarUsers();
    @Query("SELECT c FROM AppUser c WHERE c.email = :ee")
    public AppUser buscarPorMail(@Param("ee")String email);
}
