package com.libre.librerias.controladores;

import com.libre.librerias.entidades.Editorial;
import com.libre.librerias.entidades.Libro;
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
@RequestMapping("/editorial")
public class EditorialControlador {

    @Autowired
    EditorialSer editorialSer;
    @Autowired
    LibroSer libroSer;

    @GetMapping("/listaEditoriales")
    public String listarEditoriales(ModelMap modelo) {
        List<Editorial> editoriales = editorialSer.listarEditoriales();
        modelo.addAttribute("editorials", editoriales);
        return "listaEditoriales";
    }

    @PostMapping("/listaEditoriales")
    public String crearEditorial(ModelMap modelo, @RequestParam String nombre) throws Exception {
        editorialSer.crearEdit(nombre);
        modelo.put("exito", "Creacion exitosa");
        return "listaEditoriales";
    }

    @GetMapping("/crearEditor")
    public String crear(ModelMap modelo) {
        return "formEditorial";
    }

    @PostMapping("/crearEditor")
    public String crearEdit(ModelMap modelo, @RequestParam String nombre) throws Exception {
        try {
            editorialSer.crearEdit(nombre);
            modelo.put("exito", "Creacion exitosa");
            return "redirect:/editorial/listaEditoriales";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "errores";
        }
    }

    @GetMapping("/eliminarEdit")
    public String eliminarEdit(ModelMap modelo, @RequestParam String id) throws Exception {
        editorialSer.baja(id);
        modelo.put("exito", "Eliminacion exitosa");
        return "listaEditoriales";
    }

    @RequestMapping("/darBaja/{idd}")
    public String darBaja(ModelMap modelo, @PathVariable String idd) throws Exception {
        try {
            editorialSer.baja(idd);
            return "redirect:/editorial/listaEditoriales";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "errores";
        }
    }

    @RequestMapping("/darAlta/{idd}")
    public String altar(ModelMap modelo, @PathVariable String idd) throws Exception {
        try {
            editorialSer.alta(idd);
            return "redirect:/editorial/listaEditoriales";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "errores";
        }
    }

    @GetMapping("/modificar/{id}")
    public String modificar(ModelMap modelo, @PathVariable String id) throws Exception {
        modelo.put("edi", editorialSer.consultar(id));
        return "editorialModif";
    }

    @PostMapping("/modificar/{id}")
    public String modif(ModelMap modelo, @RequestParam String name, @PathVariable String id) throws Exception {
        try {
            editorialSer.modificarEdit(id, name);
            return "redirect:/editorial/listaEditoriales";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "errores";
        }
    }

    @GetMapping("/delete/{idd}")
    public String del(@PathVariable String idd, ModelMap modelo) throws Exception {
        try {
            Libro l = libroSer.consultarEdi(idd);
            if (l != null) {
                throw new Exception("La editorial esta presente en algun libro. (" + l.getTitulo() + ")");
            } else {
                editorialSer.delete(idd);
                return "redirect:/editorial/listaEditoriales";
            }

        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "errores";
        }
    }
}
