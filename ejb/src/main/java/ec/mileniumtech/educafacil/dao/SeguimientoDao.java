package ec.mileniumtech.educafacil.dao;

import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Seguimiento;

public interface SeguimientoDao extends GenericoDao<Seguimiento, Long> {
    void agregarActualizarSeguimiento(Seguimiento seguimiento);
    List<Seguimiento> listaSeguimientoMatricula(int matricula);
}

