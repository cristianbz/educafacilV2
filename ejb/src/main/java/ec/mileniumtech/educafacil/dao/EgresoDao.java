package ec.mileniumtech.educafacil.dao;

import java.util.Date;
import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.dto.DtoFlujoDinero;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Egresos;

public interface EgresoDao extends GenericoDao<Egresos, Long> {
    void agregarActualizarEgreso(Egresos egreso) throws DaoException, EntidadDuplicadaException;
    List<Egresos> listaEgresos() throws DaoException;
    List<Egresos> listaEgresosFechas(Date fechaUno, Date fechaDos) throws DaoException;
    List<DtoFlujoDinero> buscaEgresosReporteria(Date fechaInicial, Date fechaFinal) throws DaoException;
}
