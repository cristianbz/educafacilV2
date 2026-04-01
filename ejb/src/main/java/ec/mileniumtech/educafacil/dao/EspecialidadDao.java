package ec.mileniumtech.educafacil.dao;

import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Especialidad;

public interface EspecialidadDao extends GenericoDao<Especialidad, Long> {
    List<Especialidad> listaDeEspecialidades() throws DaoException;
}
