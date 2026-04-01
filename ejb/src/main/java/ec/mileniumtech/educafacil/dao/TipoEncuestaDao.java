package ec.mileniumtech.educafacil.dao;

import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.TipoEncuesta;

public interface TipoEncuestaDao extends GenericoDao<TipoEncuesta, Long> {
    List<TipoEncuesta> listaDeTiposDeEncuestas() throws DaoException;
    List<TipoEncuesta> listaDeTiposDeEncuestasPorOe(int codigo) throws DaoException;
    TipoEncuesta actualizarTipoEncuesta(TipoEncuesta tipoEncuesta) throws DaoException, EntidadDuplicadaException;
}
