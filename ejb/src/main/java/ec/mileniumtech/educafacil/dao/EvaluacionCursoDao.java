package ec.mileniumtech.educafacil.dao;

import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.EvaluacionCurso;

public interface EvaluacionCursoDao extends GenericoDao<EvaluacionCurso, Long> {
    List<EvaluacionCurso> listaDeEvaluacionesDeCurso();
    List<EvaluacionCurso> listaDeEvaluacionesPorCurso(int codigo, int codigoobj);
    List<EvaluacionCurso> listaDeEvaluacionesDeCursoActivas(int codigoC);
    EvaluacionCurso agregarEvaluacionCurso(EvaluacionCurso evaluacionCurso);
}

