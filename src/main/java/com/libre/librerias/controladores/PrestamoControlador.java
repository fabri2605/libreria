package com.libre.librerias.controladores;

import com.libre.librerias.entidades.Cliente;
import com.libre.librerias.entidades.Libro;
import com.libre.librerias.entidades.Prestamo;
import com.libre.librerias.servicios.ClienteSer;
import com.libre.librerias.servicios.LibroSer;
import com.libre.librerias.servicios.PrestamoSer;
import java.util.ArrayList;
import java.util.Date;
import org.springframework.stereotype.Controller;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/prestamo")
public class PrestamoControlador {

    @Autowired
    PrestamoSer prestamoSer;
    @Autowired
    ClienteSer clienteSer;
    @Autowired
    LibroSer libroSer;

    @GetMapping("/listar")
    public String listarClientes(ModelMap modelo) {
        List<Prestamo> presta = prestamoSer.listarPrestamos();
        modelo.addAttribute("pre", presta);
        return "listaPrestamos";
    }

    @PostMapping("/listar")
    public String buscar(ModelMap redirect, @RequestParam String id) throws Exception {

        List<Prestamo> presta = prestamoSer.listarPrestamos();
        List<Prestamo> prestap = new ArrayList();
        Prestamo pres = new Prestamo();
        String conta = id.substring(1);
        try {

            for (Prestamo pl : presta) {
                if (pl.getCliente().getNombre().trim().equalsIgnoreCase(conta)) {
                    prestap.add(pl);
                
                } else if (pl.getLibro().getTitulo().trim().equalsIgnoreCase(conta)) {
                    prestap.add(pl);
                } else if (pl.getId().equalsIgnoreCase(conta)) {
                    prestap.add(pl);
                }
            }
            redirect.addAttribute("pre", prestap);
            return "listaPrestamos";

        } catch (Exception e) {
            redirect.put("error", e.getMessage() + " " + presta.get(0).getCliente().getNombre() + " : " + (conta));
            return "errores";
        }
    }

    @GetMapping("/crearPresta")
    public String crear(ModelMap modelo, ModelMap modeloA) {
        List<Cliente> clis = clienteSer.listarClientes();
        modelo.put("cli", clis);
        List<Libro> libs = libroSer.listarLibros();
        modelo.put("lib", libs);
        return "formPresta";
    }

    @PostMapping("/crearPresta")
    public String creado(ModelMap modelo, @RequestParam String idCliente, @RequestParam String idLibro, @RequestParam String prestamo, @RequestParam String devolucion) throws Exception {
        try {
            Date pres = new Date();
            pres.setDate(Integer.parseInt(prestamo.substring(8, 10)));
            pres.setMonth(Integer.parseInt(prestamo.substring(5, 7)) - 1);
            pres.setYear(Integer.parseInt(prestamo.substring(0, 4)) - 1900);
            Date dev = new Date();
            dev.setDate(Integer.parseInt(devolucion.substring(8, 10)));
            dev.setMonth(Integer.parseInt(devolucion.substring(5, 7)) - 1);
            dev.setYear(Integer.parseInt(devolucion.substring(0, 4)) - 1900);
            Optional<Cliente> c = clienteSer.consultarName(idCliente);
            Optional<Libro> l = libroSer.consultarName(idLibro);
            prestamoSer.crearPresta(pres, dev, c.get(), l.get());
            return "redirect:/prestamo/listar";
        } catch (Exception b) {
            modelo.put("error", b.getMessage());
            return "errores";
        }
    }

    @GetMapping("/delete/{idd}")
    public String del(@PathVariable String idd, ModelMap modelo) throws Exception {
        try {
            prestamoSer.delete(idd);
            return "redirect:/prestamo/listar";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "errores";
        }
    }

    @RequestMapping("/darBaja/{id}")
    public String bajar(ModelMap modelo, @PathVariable String id) {
        try {
            prestamoSer.baja(id);
            return "redirect:/prestamo/listar";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "errores";
        }
    }

    @RequestMapping("/darAlta/{id}")
    public String altar(ModelMap modelo, @PathVariable String id) {
        try {
            prestamoSer.alta(id);
            return "redirect:/prestamo/listar";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "errores";
        }
    }

    @GetMapping("/modificar/{id}")
    public String modificar(ModelMap modelo, @PathVariable String id) throws Exception {
        modelo.addAttribute("pre", prestamoSer.consultar(id));
        List<Cliente> clis = clienteSer.listarClientes();
        modelo.put("cli", clis);
        List<Libro> libs = libroSer.listarLibros();
        modelo.put("lib", libs);
        return "prestaModif";
    }

    @PostMapping("/modificar/{id}")
    public String modif(ModelMap modelo, @PathVariable String id, @RequestParam String idCliente, @RequestParam String idLibro, @RequestParam String prestamo, @RequestParam String devolucion) throws Exception {
        try {
            Date pres = new Date();
            pres.setDate(Integer.parseInt(prestamo.substring(8, 10)));
            pres.setMonth(Integer.parseInt(prestamo.substring(5, 7)) - 1);
            pres.setYear(Integer.parseInt(prestamo.substring(0, 4)) - 1900);
            Date dev = new Date();
            dev.setDate(Integer.parseInt(devolucion.substring(8, 10)));
            dev.setMonth(Integer.parseInt(devolucion.substring(5, 7)) - 1);
            dev.setYear(Integer.parseInt(devolucion.substring(0, 4)) - 1900);
            Optional<Cliente> c = clienteSer.consultarName(idCliente);
            Optional<Libro> l = libroSer.consultarName(idLibro);
            prestamoSer.modificar(id, pres, dev, c.get(), l.get());
            return "redirect:/prestamo/listar";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "errores";
        }
    }

}
