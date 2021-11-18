package com.libre.librerias.servicios;

import com.libre.librerias.entidades.Editorial;
import com.libre.librerias.repositorios.EditorialRepo;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EditorialSer {

    // consulta, creación, modificación, listar y dar de baja
    @Autowired
    public EditorialRepo editorialRepo;

    @Transactional
    public void baja(String idEdit) throws Exception {
        if (idEdit == null || idEdit.trim().isEmpty()) {
            throw new Exception("Identificador de editorial nulo!");
        }
        Optional<Editorial> edi = editorialRepo.findById(idEdit);
        if (edi.isPresent()) {
            Editorial ed = edi.get();
            ed.setAlta(false);
            editorialRepo.save(ed);
        }
    }

    @Transactional
    public void alta(String idEdit) throws Exception {
        if (idEdit == null || idEdit.trim().isEmpty()) {
            throw new Exception("Identificador de editorial nulo!");
        }
        Optional<Editorial> edi = editorialRepo.findById(idEdit);
        if (edi.isPresent()) {
            Editorial ed = edi.get();
            ed.setAlta(true);
            editorialRepo.save(ed);
        }
    }

    @Transactional
    public void delete(String id) throws Exception {
            editorialRepo.deleteById(id);
    }

    public List<Editorial> listarEditoriales() {
        List<Editorial> editoriales = editorialRepo.listarEditoriales();
        return editoriales;
    }

    public Editorial consultar(String idEdi) throws Exception {
        try {
            if (idEdi == null || idEdi.trim().isEmpty()) {
                throw new Exception("Identificador de editorial nulo!");
            }
            Optional<Editorial> edit = editorialRepo.findById(idEdi);
            Editorial edi = edit.get();
            return edi;
        } catch (Exception e) {
            throw new Exception("Editorial no encontrada");
        }
    }

    public Editorial consultarName(String idEdi) throws Exception {
        if (idEdi == null || idEdi.trim().isEmpty()) {
            throw new Exception("Nombre de editorial nulo!");
        }
        Editorial edit = editorialRepo.buscarPorName(idEdi);
        if (edit != null) {
            return edit;
        } else {
            throw new Exception("Editorial no encontrada");
        }
    }

    @Transactional
    public void crearEdit(String nombre) throws Exception {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("Nombre de editorial nulo!");
        }
        Editorial edi = new Editorial();
        edi.setNombre(nombre);
        Editorial edd = editorialRepo.buscarPorName(nombre);
        if (edd != null) {
            if (edd.isAlta()) {
                throw new Exception("La editorial ya esta en la base de datos");
            } else {
                edd.setAlta(true);
                editorialRepo.save(edd);
            }
        } else {
            edi.setAlta(true);
            editorialRepo.save(edi);
        }
    }

    @Transactional
    public void modificarEdit(String idEdit, String name) throws Exception {
        if (idEdit == null || idEdit.trim().isEmpty()) {
            throw new Exception("Identificador de editorial nulo!");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new Exception("Nombre de editorial nulo!");
        }
        Optional<Editorial> edi = editorialRepo.findById(idEdit);
        if (edi.isPresent()) {
            Editorial ed = edi.get();
            ed.setNombre(name);
            editorialRepo.save(ed);
        } else {
            throw new Exception("La editorial no se encontro en la base de datos");
        }
    }
}
