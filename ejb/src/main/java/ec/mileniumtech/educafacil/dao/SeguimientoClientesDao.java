package ec.mileniumtech.educafacil.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.dto.DtoMatriculasCurso;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.DetalleSeguimiento;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.SeguimientoClientes;

public interface SeguimientoClientesDao extends GenericoDao<SeguimientoClientes, Long> {
    void agregarSeguimiento(SeguimientoClientes seguimiento, List<DetalleSeguimiento> detalle) throws DaoException, EntidadDuplicadaException;
    List<SeguimientoClientes> listaSeguimiento() throws DaoException;
    List<SeguimientoClientes> listaSeguimientoVendedorAsignado() throws DaoException;
    List<SeguimientoClientes> listaSeguimientoCampania(Integer campania) throws DaoException;
    List<SeguimientoClientes> listaSeguimientoCampaniaVendedor(Integer campaniaS) throws DaoException;
    BigInteger alcanceCampania(int campania) throws DaoException;
    BigInteger prospectosCampania(int campania, String estado) throws DaoException;
    List<SeguimientoClientes> listaSeguimientoCampaniaCurso(Integer curso) throws DaoException;
    List<SeguimientoClientes> listaSeguimientoCampaniaFechas(Date inicio, Date fin) throws DaoException;
    void actualizarSeguimiento(SeguimientoClientes seguimiento) throws DaoException, EntidadDuplicadaException;
    SeguimientoClientes seguimiento(int id) throws DaoException;
    SeguimientoClientes validaNumero(String telefono, int curso, int campania) throws DaoException;
    List<SeguimientoClientes> listaPendientesLlamada() throws DaoException;
    BigDecimal totalDatosCRM(String estado) throws DaoException;
    BigDecimal totalDatosCRMVendedor(String estado, Integer vendedor, Integer campania) throws DaoException;
    List<DtoMatriculasCurso> listaInteresadosCursoCRM() throws DaoException;
    List<DtoMatriculasCurso> listaEstadosContactoCursoCRM(String estado) throws DaoException;
}
