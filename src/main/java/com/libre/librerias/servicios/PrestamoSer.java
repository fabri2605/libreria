package com.libre.librerias.servicios;

import com.libre.librerias.entidades.Cliente;
import com.libre.librerias.entidades.Libro;
import com.libre.librerias.entidades.Prestamo;
import com.libre.librerias.repositorios.PrestamoRepo;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrestamoSer {

    @Autowired
    PrestamoRepo presRepo;

    public List<Prestamo> listarPrestamos() {
        List<Prestamo> editoriales = presRepo.listarPrestamos();
        return editoriales;
    }

    @Transactional
    public void crearPresta(Date pres, Date dev, Cliente cliente, Libro libro) throws Exception {
        try {
            Prestamo c = new Prestamo();
            List<Prestamo> editoriales = presRepo.listarPrestamos();
            for (Prestamo edi : editoriales) {
                if (edi.getCliente() == cliente) {
                    if (edi.getLibro() == libro) {
                        throw new Exception("El prestamo ya existe!");
                    }
                }
            }

            c.setAlta(true);
            c.setFechaDevolucion(dev);
            c.setFechaPrestamo(pres);
            c.setCliente(cliente);
            c.setLibro(libro);
            presRepo.save(c);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Transactional
    public void delete(String id) throws Exception {
        presRepo.deleteById(id);
    }

    @Transactional
    public void baja(String id) throws Exception {
        if (id == null || id.trim().isEmpty()) {
            throw new Exception("Identificador de prestamo nulo!");
        }
        Optional<Prestamo> aui = presRepo.findById(id);
        if (aui.isPresent()) {
            Prestamo au = aui.get();
            au.setAlta(false);
            presRepo.save(au);
        } else {
            throw new Exception("El prestamo no se encuentra en la base de datos");
        }
    }

    @Transactional
    public void alta(String id) throws Exception {
        if (id == null || id.trim().isEmpty()) {
            throw new Exception("Identiificador de prestamo nulo!");
        }
        Optional<Prestamo> au = presRepo.findById(id);
        if (au != null) {
            Prestamo aui = au.get();
            aui.setAlta(true);
            presRepo.save(aui);
        } else {
            throw new Exception("El prestamo no se encuentra en la base de datos");
        }
    }

    public Optional<Prestamo> consultar(String idCliente) throws Exception {
        if (idCliente == null || idCliente.trim().isEmpty()) {
            throw new Exception("Identificador de prestamo nulo!");
        }
        Optional<Prestamo> cliente = presRepo.buscar(idCliente);
        if (cliente.isPresent()) {
            return cliente;
        } else {
            throw new Exception("ERROR!");
        }
    }

    @Transactional
    public void modificar(String id, Date pres, Date dev, Cliente cliente, Libro libro) throws Exception {
        if (id == null || pres == null || dev == null || cliente == null || libro == null) {
            throw new Exception("Algun dato vacio");
        }
        Optional<Prestamo> press = presRepo.findById(id);
        if (press.isPresent()) {

            Prestamo c = press.get();
            c.setAlta(true);
            c.setCliente(cliente);
            c.setLibro(libro);
            c.setFechaDevolucion(dev);
            c.setFechaPrestamo(pres);
            presRepo.save(c);

        } else {
            throw new Exception("El prestamo no se encontro");
        }

    }
}
