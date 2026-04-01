package ec.mileniumtech.educafacil.dao;

import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Catalogo;

public interface CatalogoDao extends GenericoDao<Catalogo, Long> {
    List<Catalogo> catalogosPorTipo(String tipoCatalogo) throws DaoException;
    List<Catalogo> catalogosPorPadre(Catalogo padre) throws DaoException;
}
