package com.libre.librerias.servicios;

import com.libre.librerias.entidades.Autor;
import com.libre.librerias.entidades.Editorial;
import com.libre.librerias.entidades.Libro;
import com.libre.librerias.repositorios.AutorRepo;
import com.libre.librerias.repositorios.EditorialRepo;
import com.libre.librerias.repositorios.LibroRepo;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LibroSer {

    // consulta, creación, modificación, listar y dar de baja
    @Autowired
    public LibroRepo libroRepo;
    @Autowired
    public AutorRepo autorRepo;
    @Autowired
    public EditorialRepo editRepo;

    public List<Libro> listarLibros() {
        List<Libro> libros = libroRepo.listarLibros();
        return libros;
    }

    @Transactional
    public void baja(String idLibro) throws Exception {
        if (idLibro == null || idLibro.trim().isEmpty()) {
            throw new Exception("Identificador de libro nulo!");
        }
        Libro l = new Libro();
        Optional<Libro> libre = libroRepo.findById(idLibro);
        if (libre.isPresent()) {
            l = libre.get();
            l.setAlta(false);
            libroRepo.save(l);
        }
    }

    @Transactional
    public void alta(String idLibro) throws Exception {
        if (idLibro == null || idLibro.trim().isEmpty()) {
            throw new Exception("Identificador de libro nulo!");
        }
        Libro l = new Libro();
        Optional<Libro> libre = libroRepo.findById(idLibro);
        if (libre.isPresent()) {
            l = libre.get();
            l.setAlta(true);
            libroRepo.save(l);
        }
    }

    @Transactional
    public void modificarLibro(String idLibro, Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresP, String idAutor, String idEditorial) throws Exception {
        validar(isbn, titulo, anio, ejemplares, ejemplaresP);
        Optional<Libro> libre = libroRepo.findById(idLibro);
        Optional<Autor> autor = autorRepo.findById(idAutor);
        Optional<Editorial> editorial = editRepo.findById(idEditorial);
        if (libre.isPresent()) {
            if (autor.isPresent()) {
                if (editorial.isPresent()) {
                    Autor auto = autor.get();
                    Editorial edit = editorial.get();
                    Libro libro = libre.get();
                    libro.setAlta(true);
                    libro.setAnio(anio);
                    libro.setAutor(auto);
                    libro.setEditorial(edit);
                    libro.setEjemplares(ejemplares);
                    libro.setEjemplaresPrestados(ejemplaresP);
                    libro.setEjemplaresRestantes((ejemplares - ejemplaresP));
                    libro.setIsbn(isbn);
                    libro.setTitulo(titulo);
                    libroRepo.save(libro);
                } else {
                    throw new Exception("La editorial no se encontro");
                }
            } else {
                throw new Exception("El autor no se encontro");
            }
        } else {
            throw new Exception("No se encontro el libro");
        }

    }

    public Libro consultar(String idLibro) throws Exception {
        if (idLibro == null || idLibro.trim().isEmpty()) {
            throw new Exception("Identificador de libro nulo!");
        }
        Optional<Libro> libro = libroRepo.findById(idLibro);
        if (libro.isPresent()) {
            Libro libri = libro.get();
            return libri;
        } else {
            throw new Exception("El libro no se encontro!");
        }
    }

    public Optional<Libro> consultarName(String idLibro) throws Exception {
        if (idLibro == null || idLibro.trim().isEmpty()) {
            throw new Exception("Identificador de libro nulo!");
        }
        try {
            Optional<Libro> libro = libroRepo.buscarPorTitulo(idLibro);
            if (libro != null) {
                return libro;
            }

        } catch (Exception e) {
            throw new Exception("El libro no se encontro!");
        }
        return null;
    }

    public Libro consultarEdi(String idLibro) throws Exception {
        if (idLibro == null || idLibro.trim().isEmpty()) {
            throw new Exception("Identificador de editorial nulo!");
        }
        List<Libro> libros = libroRepo.listarLibros();
        Editorial a = editRepo.getById(idLibro);
        for (Libro libro : libros) {
            if (libro.getEditorial() == a) {
                return libro;
            }
        }
        return null;
    }

    public Libro consultarAut(String idLibro) throws Exception {
        if (idLibro == null || idLibro.trim().isEmpty()) {
            throw new Exception("Identificador de autor nulo!");
        }
        List<Libro> libros = libroRepo.listarLibros();
        Autor a = autorRepo.getById(idLibro);
        for (Libro libro : libros) {
            if (libro.getAutor() == a) {
                return libro;
            }
        }
        return null;

    }

    @Transactional
    public void crearLibro(Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresP, String idAutor, String idEditorial) throws Exception {
        validar(isbn, titulo, anio, ejemplares, ejemplaresP);
        Optional<Autor> autor = autorRepo.findById(idAutor);
        Optional<Editorial> editorial = editRepo.findById(idEditorial);
        Libro lib = new Libro();
        Optional<Libro> libro = libroRepo.buscarPorTitulo(titulo);
        if (libro != null) {
            throw new Exception("El libro ya existe!");
        } else {
            lib.setTitulo(titulo);
            if (autor.isPresent()) {
                if (editorial.isPresent()) {
                    Autor auto = autor.get();
                    Editorial edit = editorial.get();
                    lib.setAutor(auto);
                    lib.setEditorial(edit);
                    lib.setAlta(true);
                    lib.setAnio(anio);
                    lib.setEjemplares(ejemplares);
                    lib.setEjemplaresPrestados(ejemplaresP);
                    lib.setEjemplaresRestantes((ejemplares - ejemplaresP));
                    lib.setIsbn(isbn);
                    lib.setTitulo(titulo);
                    libroRepo.save(lib);
                } else {
                    throw new Exception("La editorial no se encontro");
                }
            } else {
                throw new Exception("El autor no se encontro");
            }

        }

    }

    @Transactional
    public void delete(String id) throws Exception {
        libroRepo.deleteById(id);
    }

    public void validar(Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresP) throws Exception {
        if (isbn == null) {
            throw new Exception("El isbn no puede estar vacio");
        }
        if (titulo == null) {
            throw new Exception("El titulo no puede estar vacio");
        }
        if (anio == null) {
            throw new Exception("El año no puede estar vacio");
        }
        if (ejemplares == null) {
            throw new Exception("Los ejemplares no pueden estar vacios");
        }
        if (ejemplaresP == null) {
            throw new Exception("Los ejemplares prestados no pueden estar vacios");
        }
    }

}
