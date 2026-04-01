package ec.mileniumtech.educafacil.dao;

import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.EvaluacionCurso;

public interface EvaluacionCursoDao extends GenericoDao<EvaluacionCurso, Long> {
    List<EvaluacionCurso> listaDeEvaluacionesDeCurso() throws DaoException;
    List<EvaluacionCurso> listaDeEvaluacionesPorCurso(int codigo, int codigoobj) throws DaoException;
    List<EvaluacionCurso> listaDeEvaluacionesDeCursoActivas(int codigoC) throws DaoException;
    EvaluacionCurso agregarEvaluacionCurso(EvaluacionCurso evaluacionCurso) throws DaoException, EntidadDuplicadaException;
}
