package ec.mileniumtech.educafacil.dao;

import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.TipoEncuesta;

public interface TipoEncuestaDao extends GenericoDao<TipoEncuesta, Long> {
    List<TipoEncuesta> listaDeTiposDeEncuestas();
    List<TipoEncuesta> listaDeTiposDeEncuestasPorOe(int codigo);
    TipoEncuesta actualizarTipoEncuesta(TipoEncuesta tipoEncuesta);
}

