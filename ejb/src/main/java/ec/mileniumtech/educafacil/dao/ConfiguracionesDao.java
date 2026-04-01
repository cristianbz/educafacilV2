package ec.mileniumtech.educafacil.dao;

import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Configuraciones;

public interface ConfiguracionesDao extends GenericoDao<Configuraciones, Long> {
    List<Configuraciones> listaConfiguraciones() throws DaoException;
}
