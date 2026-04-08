package ec.mileniumtech.educafacil.dao;

import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Area;

public interface AreaDao extends GenericoDao<Area, Long> {
    List<Area> listaDeAreas();
}

