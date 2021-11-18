package com.libre.librerias.controladores;

import com.libre.librerias.entidades.Autor;
import com.libre.librerias.entidades.Editorial;
import com.libre.librerias.entidades.Libro;
import com.libre.librerias.servicios.AutorSer;
import com.libre.librerias.servicios.EditorialSer;
import com.libre.librerias.servicios.LibroSer;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/libro")
public class LibroControlador {

    @Autowired
    LibroSer libroSer;
    @Autowired
    AutorSer aSer;
    @Autowired
    EditorialSer eSer;

    @GetMapping("/listar")
    public String listar(ModelMap modelo) {
        List<Libro> libros = libroSer.listarLibros();
        modelo.addAttribute("lib", libros);
        return "listaLibros";
    }

    @GetMapping("/crearLibro")
    public String crear(ModelMap modelo, ModelMap modeloA) {
        List<Editorial> edits = eSer.listarEditoriales();
        modelo.put("edits", edits);
        List<Autor> auts = aSer.listar();
        modelo.put("auts", auts);
        return "formLibro";
    }

    @PostMapping("/crearLibro")
    public String creado(ModelMap modelo, @RequestParam Long isbn, @RequestParam String titulo, @RequestParam Integer anio, @RequestParam Integer ejemplares, @RequestParam String idAutor, @RequestParam String idEditorial) throws Exception {
        try {
            Autor a = aSer.consultar(idAutor, 2);
            Editorial e = eSer.consultarName(idEditorial);
            libroSer.crearLibro(isbn, titulo, anio, ejemplares, 0, a.getId(), e.getId());
            return "redirect:/libro/listar";
        } catch (Exception b) {
            modelo.put("error", b.getMessage());
            return "errores";
        }
    }

    @RequestMapping("/darAlta/{id}")
    public String alta(ModelMap modelo, @PathVariable String id) throws Exception {
        try {
            libroSer.alta(id);
            return "redirect:/libro/listar";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "errores";
        }
    }

    @RequestMapping("/darBaja/{id}")
    public String bajar(ModelMap modelo, @PathVariable String id) throws Exception {
        try {
            libroSer.baja(id);
            return "redirect:/libro/listar";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "errores";
        }

    }
    
    @GetMapping("/modificar/{id}")
    public String modificar(ModelMap modelo, @PathVariable String id) throws Exception {
        modelo.addAttribute("edits", eSer.listarEditoriales());
        modelo.addAttribute("auts", aSer.listar());
        modelo.addAttribute("libro", libroSer.consultar(id));
        return "libroModif";
    }
    
    @PostMapping("/modificar/{id}")
    public String modif(ModelMap modelo, @PathVariable String id, @RequestParam String titulo, @RequestParam Integer anio, @RequestParam Integer ejemplares, @RequestParam Integer ejemplaresP, @RequestParam String idEditorial, @RequestParam String idAutor, @RequestParam Long isbn) throws Exception {
        try {
            Autor a = aSer.consultar(idAutor, 2);
            Editorial e = eSer.consultarName(idEditorial);
            libroSer.modificarLibro(id, isbn, titulo, anio, ejemplares, ejemplaresP, a.getId(), e.getId());
            modelo.put("exito", "Se creo correctamente");
            return "redirect:/libro/listar";
        }catch(Exception e){
            modelo.put("error", e.getMessage());
            return "errores";
        }
    }
    
    @GetMapping("/delete/{idd}")
    public String del(@PathVariable String idd, ModelMap modelo) throws Exception {
        try {
            libroSer.delete(idd);
            return "redirect:/libro/listar";
        }catch(Exception e){
            modelo.addAttribute("error", e.getMessage());
            return "errores";
        }
    }
}
