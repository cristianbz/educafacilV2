package ec.mileniumtech.educafacil.dao;

import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Estudiante;

public interface EstudianteDao extends GenericoDao<Estudiante, Long> {
    List<Estudiante> estudiantesPorApellido(String apellidos) throws DaoException;
    Estudiante estudiantesPorCedula(String cedula) throws DaoException;
    void actualizaEstudiante(Estudiante estudiante) throws DaoException, EntidadDuplicadaException;
}
