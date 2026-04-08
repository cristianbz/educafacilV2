package ec.mileniumtech.educafacil.dao;

import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Instructor;

public interface InstructorDao extends GenericoDao<Instructor, Long> {
    List<Instructor> listaInstructores();
    void agregarActualizarInstructor(Instructor instructor);
}
