package com.libre.librerias.controladores;

import com.libre.librerias.entidades.Autor;
import com.libre.librerias.entidades.Libro;
import com.libre.librerias.servicios.AutorSer;
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
@RequestMapping("/autor")
public class AutorControlador {

    @Autowired
    AutorSer autorSer;
    @Autowired
    LibroSer libroSer;

    @GetMapping("/listaAutores")
    public String lista(ModelMap modelo) {
        List<Autor> autores = autorSer.listar();
        modelo.addAttribute("autores", autores);
        return "listaAutores";
    }

    @GetMapping("/crearAutor")
    public String form(ModelMap modelo) throws Exception {
        return "formAutor";
    }

    @PostMapping("/crearAutor")
    public String crear(ModelMap modelo, @RequestParam String name) throws Exception {
        try {
            autorSer.crearAutor(name);
            modelo.addAttribute("exito", "Salio bien!");
            return "redirect:/autor/listaAutores";
        }catch(Exception e){
            modelo.addAttribute("error", e.getMessage());
            return "errores";
        }
    }
    
    @RequestMapping("/darBaja/{nombre}")
    public String bajar(ModelMap modelo, @PathVariable String nombre){
        try {
            autorSer.baja(nombre);
            return "redirect:/autor/listaAutores";
        }catch(Exception e){
            modelo.addAttribute("error", e.getMessage());
            return "errores";
        }
    }
    
    @RequestMapping("/darAlta/{nombre}")
    public String altar(ModelMap modelo, @PathVariable String nombre){
        try {
            autorSer.alta(nombre);
            return "redirect:/autor/listaAutores";
        }catch(Exception e){
            modelo.addAttribute("error", e.getMessage());
            return "errores";
        }
    }
    
    @GetMapping("/modificar/{id}")
    public String modificar(ModelMap modelo, @PathVariable String id) throws Exception {
        modelo.put("autor", autorSer.consultar(id,1));
        return "autorModif";
    }
    
    @PostMapping("/modificar/{id}")
    public String modif(ModelMap modelo, @RequestParam String name, @PathVariable String id) throws Exception {
        try {
            autorSer.modificarAutor(id, name);
            return "redirect:/autor/listaAutores";
        }catch(Exception e){
            modelo.addAttribute("error", e.getMessage());
            return "errores";
        }
    }
    
    @GetMapping("/delete/{id}")
    public String del(ModelMap modelo, @PathVariable String id) throws Exception {
        try {
            Libro l = libroSer.consultarAut(id);
            if ( l != null ) {
                throw new Exception("El autor esta presente en algun libro. ("+l.getTitulo()+")");
            }else{
            autorSer.delete(id);
            return "redirect:/autor/listaAutores";
            }
        }catch(Exception e){
            modelo.addAttribute("error", e.getMessage());
            return "errores";
        }
    }
}
