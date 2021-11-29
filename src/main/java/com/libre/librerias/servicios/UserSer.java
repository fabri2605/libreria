package com.libre.librerias.servicios;

import com.libre.librerias.entidades.AppUser;
import com.libre.librerias.entidades.UserRole;
import com.libre.librerias.repositorios.UserRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UserSer implements UserDetailsService {

    @Autowired
    UserRepo userR;

    public List<AppUser> listarUsers() {
        List<AppUser> users = userR.listarUsers();
        return users;
    }

    @Transactional(readOnly = true)
    public AppUser buscarPorId(String id) throws Exception {

        Optional<AppUser> respuesta = userR.findById(id);
        if (respuesta.isPresent()) {

            AppUser usuario = respuesta.get();
            return usuario;
        } else {

            throw new Exception("No se encontró el usuario solicitado");
        }

    }

    public void registrar(String username, String name, String surname, String password) throws Exception {
        validar(username, name, surname, password);
        List<AppUser> users = listarUsers();
        for (AppUser user : users) {
            if (user.getEmail().equalsIgnoreCase(username)) {
                throw new Exception("El usuario ya esta registrado!");
            }
        }
        try {

            AppUser u = new AppUser();
            u.setEmail(username);
            u.setName(name);
            u.setSurname(surname);
            u.setRol(UserRole.USER);
            String encriptada = new BCryptPasswordEncoder().encode(password);
            u.setPassword(encriptada);
            userR.save(u);
        } catch (Exception e) {
            throw new Exception("Error creando user!");
        }
    }

    public void modif(String id, String name, String surname, String contra, String contranew) throws Exception {
        validar("..", name, surname, contra);
        if (contra == null || contra.isEmpty()) {
            throw new Exception("Contraseña invalida!");
        }
        if (contranew.length()<5 && contranew.length()!=0) {
            throw new Exception("La contraseña debe tener un minimo de 6 caracteres!");
        }
        if (contranew.length()==0) {
            contranew = contra;
        }
        List<AppUser> users = listarUsers();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        for (AppUser user : users) {
            
            if (user.getId().equalsIgnoreCase(id)) {
                if (encoder.matches(contra, user.getPassword())) {
                    try {
                        user.setName(name);
                        user.setSurname(surname);
                        String encriptada = new BCryptPasswordEncoder().encode(contranew);
                        user.setPassword(encriptada);
                        userR.save(user);
                    } catch (Exception e) {
                        throw new Exception("Error modificando usuario!");
                    }

                } else {
                    throw new Exception("Contraseña incorrecta " + user.getPassword() + " " + contra);
                }
            }
        }
    }

    public void validar(String username, String name, String surname, String password) throws Exception {
        if (username == null || username.isEmpty()) {
            throw new Exception("Email invalido!");
        }
        if (name == null || name.isEmpty()) {
            throw new Exception("Nombre Invalido!");
        }
        if (surname == null || surname.isEmpty()) {
            throw new Exception("Apellido Invalido!");
        }
        if (password == null || password.isEmpty()) {
            throw new Exception("Ingrese la contraseña!");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser usuario = userR.buscarPorMail(email);
        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList<>();

            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_" + usuario.getRol());
            permisos.add(p1);

            //Esto me permite guardar el OBJETO USUARIO LOG, para luego ser utilizado
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);
            session.setMaxInactiveInterval(30000000);
            session.setAttribute("timeOutTimeInSeconds", 300);
            session.setAttribute("showTimerTimeInSeconds", 30);

            User user = new User(usuario.getEmail(), usuario.getPassword(), permisos);
            return user;

        } else {
            return null;
        }
    }
}
