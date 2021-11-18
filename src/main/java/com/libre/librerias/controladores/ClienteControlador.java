

package com.libre.librerias.controladores;

import com.libre.librerias.entidades.Cliente;
import com.libre.librerias.servicios.ClienteSer;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/cliente")
public class ClienteControlador {
    
    @Autowired
    ClienteSer clienteSer;
    
    @GetMapping("/listar")
    public String listarClientes(ModelMap modelo) {
        List<Cliente> clientes = clienteSer.listarClientes();
        modelo.addAttribute("client", clientes);
        return "listaClientes";
    }
    
    @GetMapping("/crearCliente")
    public String crear(ModelMap modelo, ModelMap modeloA) {
        return "formClient";
    }
    
    @PostMapping("/crearCliente")
    public String creado(ModelMap modelo, @RequestParam String nombre, @RequestParam String apellido, @RequestParam Long dni, @RequestParam String telefono) throws Exception {
        try {
            clienteSer.crearCli(nombre, apellido, telefono, dni);
            return "redirect:/cliente/listar";
        } catch (Exception b) {
            modelo.put("error", b.getMessage());
            return "errores";
        }
    }
    
    @RequestMapping("/darBaja/{id}")
    public String bajar(ModelMap modelo, @PathVariable String id){
        try {
            clienteSer.baja(id);
            return "redirect:/cliente/listar";
        }catch(Exception e){
            modelo.addAttribute("error", e.getMessage());
            return "errores";
        }
    }
    
    @RequestMapping("/darAlta/{id}")
    public String altar(ModelMap modelo, @PathVariable String id){
        try {
            clienteSer.alta(id);
            return "redirect:/cliente/listar";
        }catch(Exception e){
            modelo.addAttribute("error", e.getMessage());
            return "errores";
        }
    }
    
    @GetMapping("/modificar/{id}")
    public String modificar(ModelMap modelo, @PathVariable String id) throws Exception {
        modelo.addAttribute("cli", clienteSer.consultar(id));
        return "clienteModif";
    }
    
    @PostMapping("/modificar/{id}")
    public String modif(ModelMap modelo,@PathVariable String id, @RequestParam String nombre, @RequestParam String apellido, @RequestParam Long dni, @RequestParam String telefono) throws Exception {
        try {
            clienteSer.modificar(id, nombre, apellido, telefono, dni);
            return "redirect:/cliente/listar";
        }catch(Exception e){
            modelo.put("error", e.getMessage());
            return "errores";
        }
    }
    
    @GetMapping("/delete/{idd}")
    public String del(@PathVariable String idd, ModelMap modelo) throws Exception {
        try {
            clienteSer.delete(idd);
            return "redirect:/cliente/listar";
        }catch(Exception e){
            modelo.addAttribute("error", e.getMessage());
            return "errores";
        }
    }
}
