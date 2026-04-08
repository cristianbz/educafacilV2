package ec.mileniumtech.educafacil.dao;

import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.dto.DtoEncuestas;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.TipoEncuestaPregunta;

public interface TipoEncuestaPreguntaDao extends GenericoDao<TipoEncuestaPregunta, Long> {
    List<TipoEncuestaPregunta> listaDePreguntas(int codigoP);
    List<TipoEncuestaPregunta> listaDeTiposDeEncuestas();
    List<TipoEncuestaPregunta> listaDeEncuestas(int codigoT);
    List<TipoEncuestaPregunta> listaPorTipoDeEncuestas(int codigoTipo);
    TipoEncuestaPregunta agregarActualizarTipoEncuestaPregunta(TipoEncuestaPregunta tipoEncuestaPregunta);
    List<DtoEncuestas> guardarRespuestasEncuestas(int encuesta);
}

