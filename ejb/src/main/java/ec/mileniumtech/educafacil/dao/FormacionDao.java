package ec.mileniumtech.educafacil.dao;

import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Formacion;

public interface FormacionDao extends GenericoDao<Formacion, Long> {
    void agregaActualizaFormacion(Formacion formacion) throws DaoException, EntidadDuplicadaException;
    List<Formacion> listaFormaciones(int codigoInstructor) throws DaoException;
}
