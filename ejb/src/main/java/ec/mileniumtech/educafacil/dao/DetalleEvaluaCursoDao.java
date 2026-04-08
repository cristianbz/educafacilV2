package ec.mileniumtech.educafacil.dao;

import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.DetalleEvaluaCurso;

public interface DetalleEvaluaCursoDao extends GenericoDao<DetalleEvaluaCurso, Long> {
    List<DetalleEvaluaCurso> listaDeDetallesDeEvaluacionDeCursos();
    void guardarEncuesta(DetalleEvaluaCurso detalle);
}

