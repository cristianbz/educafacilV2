package ec.mileniumtech.educafacil.dao;

import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Persona;

public interface PersonaDao extends GenericoDao<Persona, Long> {
    Persona buscarPersonaPorCedula(String cedula) throws DaoException;
    void agregarPersona(Persona persona) throws DaoException, EntidadDuplicadaException;
    Persona actualizarPersona(Persona persona) throws DaoException, EntidadDuplicadaException;
    List<Persona> buscarPersonaPorApellidos(String apellidos) throws DaoException;
    Persona buscarPersonaPorId(int codigo) throws DaoException;
    Persona buscarPersonaPorCedulaCorreo(String cedula, String correo) throws DaoException;
}
