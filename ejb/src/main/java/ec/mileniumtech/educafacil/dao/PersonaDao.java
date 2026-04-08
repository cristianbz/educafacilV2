package ec.mileniumtech.educafacil.dao;

import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Persona;

public interface PersonaDao extends GenericoDao<Persona, Long> {
    Persona buscarPersonaPorCedula(String cedula);
    void agregarPersona(Persona persona);
    Persona actualizarPersona(Persona persona);
    List<Persona> buscarPersonaPorApellidos(String apellidos);
    Persona buscarPersonaPorId(int codigo);
    Persona buscarPersonaPorCedulaCorreo(String cedula, String correo);
}

