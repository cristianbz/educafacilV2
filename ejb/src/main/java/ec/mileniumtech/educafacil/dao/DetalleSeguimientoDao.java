package ec.mileniumtech.educafacil.dao;

import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.DetalleSeguimiento;

public interface DetalleSeguimientoDao extends GenericoDao<DetalleSeguimiento, Long> {
    void agregarDetalle(DetalleSeguimiento detalle);
    List<DetalleSeguimiento> listaDetalle(Integer seguimiento);
}

