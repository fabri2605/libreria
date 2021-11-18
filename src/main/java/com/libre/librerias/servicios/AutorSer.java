package com.libre.librerias.servicios;

import com.libre.librerias.entidades.Autor;
import com.libre.librerias.repositorios.AutorRepo;
import com.libre.librerias.repositorios.LibroRepo;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutorSer {

    // consulta, creación, modificación, listar y dar de baja
    @Autowired
    public AutorRepo autorRepo;

    public List<Autor> listar() {
        List<Autor> autores = autorRepo.listarAutores();
        return autores;
    }

    @Transactional
    public void crearAutor(String name) throws Exception {
        if (name == null || name.trim().isEmpty()) {
            throw new Exception("Nombre de autor nulo!");
        }
        Autor bb = new Autor();
        bb.setNombre(name);
        Autor au = autorRepo.buscarPorName(name);
        if (au != null) {
            if (au.isAlta()) {
                throw new Exception("El autor ya se encuentra en la base de datos");
            } else {
                au.setAlta(true);
                autorRepo.save(au);
            }
        } else {
            bb.setAlta(true);
            autorRepo.save(bb);
        }
    }

    @Transactional
    public void modificarAutor(String idAutor, String name) throws Exception {
        if (name == null || name.trim().isEmpty()) {
            throw new Exception("Nombre de autor nulo!");
        }
        if (idAutor == null || idAutor.trim().isEmpty()) {
            throw new Exception("Identificador de autor nulo!");
        }
        Optional<Autor> au = autorRepo.findById(idAutor);
        if (au.isPresent()) {
            Autor aui = au.get();
            aui.setNombre(name);
            autorRepo.save(aui);
        } else {
            throw new Exception("El autor no se encuentra en la base de datos");
        }
    }

    @Transactional
    public void baja(String nombre) throws Exception {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("Nombre de autor nulo!");
        }
        Autor aui = autorRepo.buscarPorName(nombre);
        if (aui != null) {
            aui.setAlta(false);
            autorRepo.save(aui);
        } else {
            throw new Exception("El autor no se encuentra en la base de datos");
        }
    }

    @Transactional
    public void alta(String nombre) throws Exception {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("Nombre de autor nulo!");
        }
        Autor aui = autorRepo.buscarPorName(nombre);
        if (aui != null) {
            aui.setAlta(true);
            autorRepo.save(aui);
        } else {
            throw new Exception("El autor no se encuentra en la base de datos");
        }
    }
    
    @Transactional
    public void delete(String nombre) throws Exception {
        
        autorRepo.deleteById(nombre);
    }

    public Autor consultar(String idAutor, Integer x) throws Exception {
        try {
            if (x == 1) {
                Optional<Autor> auto = autorRepo.findById(idAutor);
                if (auto.isPresent()) {
                    Autor autor = auto.get();
                    return autor;
                } else {
                    throw new Exception("El autor no se encuentra en la base de datos");
                }
            } else if (x == 2) {
                Autor a = autorRepo.buscarPorName(idAutor);
                if (a != null) {
                    return a;
                } else {
                    throw new Exception("El autor no se encuentra en la base de datos");
                }
            }
        } catch (Exception e) {
            throw new Exception("El autor no se encuentra en la base de datos");
        }
        return null;
    }
}
