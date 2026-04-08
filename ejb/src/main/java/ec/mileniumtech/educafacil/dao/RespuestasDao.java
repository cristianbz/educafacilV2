package ec.mileniumtech.educafacil.dao;

import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Respuestas;

public interface RespuestasDao extends GenericoDao<Respuestas, Long> {
    List<Respuestas> listaRespuestas();
    Respuestas agregActualizarRespuestas(Respuestas respuestas);
    List<Respuestas> listaRespuestasPorCategoria(int codigoCategoria);
}

