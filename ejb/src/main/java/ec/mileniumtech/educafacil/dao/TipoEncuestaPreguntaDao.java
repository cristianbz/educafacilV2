package ec.mileniumtech.educafacil.dao;

import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.dto.DtoEncuestas;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.TipoEncuestaPregunta;

public interface TipoEncuestaPreguntaDao extends GenericoDao<TipoEncuestaPregunta, Long> {
    List<TipoEncuestaPregunta> listaDePreguntas(int codigoP) throws DaoException;
    List<TipoEncuestaPregunta> listaDeTiposDeEncuestas() throws DaoException;
    List<TipoEncuestaPregunta> listaDeEncuestas(int codigoT) throws DaoException;
    List<TipoEncuestaPregunta> listaPorTipoDeEncuestas(int codigoTipo) throws DaoException;
    TipoEncuestaPregunta agregarActualizarTipoEncuestaPregunta(TipoEncuestaPregunta tipoEncuestaPregunta) throws DaoException, EntidadDuplicadaException;
    List<DtoEncuestas> guardarRespuestasEncuestas(int encuesta) throws DaoException;
}
