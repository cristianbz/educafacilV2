package ec.mileniumtech.educafacil.dao;

import java.util.Date;
import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.dto.DtoFlujoDinero;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.DetallePagos;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Pagos;

public interface PagosDao extends GenericoDao<Pagos, Long> {
    void agregarPago(Pagos pago) throws DaoException, EntidadDuplicadaException;
    List<DetallePagos> buscaPagosPorMatricula(int codigoMatricula) throws DaoException;
    List<DtoFlujoDinero> buscaIngresosReporteria(Date fechaInicial, Date fechaFinal) throws DaoException;
}
