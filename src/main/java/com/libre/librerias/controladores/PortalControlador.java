package com.libre.librerias.controladores;

import com.libre.librerias.entidades.AppUser;
import com.libre.librerias.servicios.UserSer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    UserSer userS;

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/inicio")
    public String ini() {
        return "index.html";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout, ModelMap model) {
        if (error != null) {
            model.put("error", "Usuario o clave incorrectos");
        }
        if (logout != null) {
            model.put("logout", "Ha salido correctamente.");
        }
        return "login";
    }

    @GetMapping("/register")
    public String registrar() {
        return "register.html";
    }

    @GetMapping("/perfil/{id}")
    public String perfil(@PathVariable String id, ModelMap modelo) throws Exception {
        AppUser u = userS.buscarPorId(id);
        modelo.addAttribute("user", u);
        return "PerfilUser.html";
    }

    @PostMapping("/register")
    public String registrado(ModelMap modelo, @RequestParam String username, @RequestParam String name, @RequestParam String surname, @RequestParam String password) {
        try {
            userS.registrar(username, name, surname, password);
            return "index";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "register.html";
        }
    }

    @GetMapping("/modificar/{id}")
    public String modificar(ModelMap modelo, @PathVariable String id) throws Exception {
        modelo.put("user", userS.buscarPorId(id));
        return "userModif";
    }

    @PostMapping("/modificar/{id}")
    public String modif(ModelMap modelo, @RequestParam String name, @PathVariable String id, @RequestParam String surname, @RequestParam String contra, @RequestParam String contranew) throws Exception {
        try {
            userS.modif(id, name, surname, contra, contranew);
            AppUser u = userS.buscarPorId(id);
            modelo.addAttribute("user", u);
            modelo.addAttribute("exito", "Usuario modificado correctamente");
            return "userModif";
        } catch (Exception e) {
            modelo.put("user", userS.buscarPorId(id));
            modelo.addAttribute("error", e.getMessage());
            return "userModif";
        }
    }
}
