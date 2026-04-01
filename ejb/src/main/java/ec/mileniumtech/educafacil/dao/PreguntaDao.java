package ec.mileniumtech.educafacil.dao;

import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Pregunta;

public interface PreguntaDao extends GenericoDao<Pregunta, Long> {
    List<Pregunta> listaDePreguntas() throws DaoException;
    Pregunta agregarActualizarPregunta(Pregunta pregunta) throws DaoException, EntidadDuplicadaException;
    List<Pregunta> listaPreguntasPorCategoria(int codigoCategoriaP) throws DaoException;
}
