package ec.mileniumtech.educafacil.dao;

import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Capacitacion;

public interface CapacitacionDao extends GenericoDao<Capacitacion, Long> {
    void agregarActualizarCapacitacion(Capacitacion capacitacion) throws DaoException, EntidadDuplicadaException;
    List<Capacitacion> listaCapacitaciones(int codigoInstructor) throws DaoException;
}
