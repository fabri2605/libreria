package com.libre.librerias.servicios;

import com.libre.librerias.entidades.Cliente;
import com.libre.librerias.repositorios.ClienteRepo;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteSer {

    @Autowired
    ClienteRepo clienRepo;

    public List<Cliente> listarClientes() {
        List<Cliente> editoriales = clienRepo.listarClientes();
        return editoriales;
    }

    @Transactional
    public void crearCli(String nombre, String apellido, String telefono, Long dni) throws Exception {
        try {
            validar(nombre, apellido, telefono, dni);
            Cliente c = new Cliente();
            Optional<Cliente> cli = clienRepo.buscarPorNombre(nombre);
            if (cli != null) {
                throw new Exception("El cliente ya existe!");
            } else {
                c.setAlta(true);
                c.setApellido(apellido);
                c.setNombre(nombre);
                c.setDocumento(dni);
                c.setTelefono(telefono);
                clienRepo.save(c);
            }

        } catch (Exception e) {
            throw new Exception("Error creando cliente!");
        }
    }

    @Transactional
    public void baja(String id) throws Exception {
        if (id == null || id.trim().isEmpty()) {
            throw new Exception("Identificador de cliente nulo!");
        }
        Optional<Cliente> aui = clienRepo.findById(id);
        if (aui.isPresent()) {
            Cliente au = aui.get();
            au.setAlta(false);
            clienRepo.save(au);
        } else {
            throw new Exception("El cliente no se encuentra en la base de datos");
        }
    }

    @Transactional
    public void alta(String id) throws Exception {
        if (id == null || id.trim().isEmpty()) {
            throw new Exception("Identiificador de cliente nulo!");
        }
        Optional<Cliente> au = clienRepo.findById(id);
        if (au != null) {
            Cliente aui = au.get();
            aui.setAlta(true);
            clienRepo.save(aui);
        } else {
            throw new Exception("El cliente no se encuentra en la base de datos");
        }
    }

    public Cliente consultar(String idCliente) throws Exception {
        if (idCliente == null || idCliente.trim().isEmpty()) {
            throw new Exception("Identificador de cliente nulo!");
        }
        Optional<Cliente> cliente = clienRepo.findById(idCliente);
        if (cliente.isPresent()) {
            Cliente cli = cliente.get();
            return cli;
        } else {
            throw new Exception("El cliente no se encontro!");
        }
    }
    
    public Optional<Cliente> consultarName(String idCliente) throws Exception {
        if (idCliente == null || idCliente.trim().isEmpty()) {
            throw new Exception("Nombre de cliente nulo!");
        }
        Optional<Cliente> cliente = clienRepo.buscarPorNombre(idCliente);
        if (cliente != null) {
            return cliente;
        } else {
            throw new Exception("El cliente no se encontro!");
        }
    }

    @Transactional
    public void modificar(String id, String nombre, String apellido, String telefono, Long dni) throws Exception {
        validar(nombre, apellido, telefono, dni);
        Optional<Cliente> cliente = clienRepo.findById(id);
        if (cliente.isPresent()) {
            Cliente c = cliente.get();
            c.setAlta(true);
            c.setApellido(apellido);
            c.setNombre(nombre);
            c.setTelefono(telefono);
            c.setDocumento(dni);
            clienRepo.save(c);
        } else {
            throw new Exception("Error modificando cliente");
        }

    }
    
    @Transactional
    public void delete(String id) throws Exception {
            clienRepo.deleteById(id);
    }

    public void validar(String nombre, String apellido, String telefono, Long dni) throws Exception {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("Nombre nulo!");
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new Exception("Apellido nulo!");
        }
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new Exception("Telefono nulo!");
        }
        if (dni == null) {
            throw new Exception("Dni nulo!");
        }
    }
}
