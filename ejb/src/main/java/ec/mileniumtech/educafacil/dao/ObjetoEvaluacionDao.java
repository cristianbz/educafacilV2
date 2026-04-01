package ec.mileniumtech.educafacil.dao;

import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.ObjetoEvaluacion;

public interface ObjetoEvaluacionDao extends GenericoDao<ObjetoEvaluacion, Long> {
    List<ObjetoEvaluacion> listaDeObjetosDeEvaluacion() throws DaoException;
    ObjetoEvaluacion actualizarObjetoEvaluacion(ObjetoEvaluacion objetoEvaluacion) throws DaoException, EntidadDuplicadaException;
}
