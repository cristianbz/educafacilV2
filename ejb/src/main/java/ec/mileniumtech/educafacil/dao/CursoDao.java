package ec.mileniumtech.educafacil.dao;

import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Curso;

public interface CursoDao extends GenericoDao<Curso, Long> {
    List<Curso> listaCursos() throws DaoException;
    List<Curso> listaOfertaCursosActivos() throws DaoException;
}
