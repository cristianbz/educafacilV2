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
    void agregarSeguimiento(SeguimientoClientes seguimiento, List<DetalleSeguimiento> detalle);
    List<SeguimientoClientes> listaSeguimiento();
    List<SeguimientoClientes> listaSeguimientoVendedorAsignado();
    List<SeguimientoClientes> listaSeguimientoCampania(Integer campania);
    List<SeguimientoClientes> listaSeguimientoCampaniaVendedor(Integer campaniaS);
    BigInteger alcanceCampania(int campania);
    BigInteger prospectosCampania(int campania, String estado);
    List<SeguimientoClientes> listaSeguimientoCampaniaCurso(Integer curso);
    List<SeguimientoClientes> listaSeguimientoCampaniaFechas(Date inicio, Date fin);
    void actualizarSeguimiento(SeguimientoClientes seguimiento);
    SeguimientoClientes seguimiento(int id);
    SeguimientoClientes validaNumero(String telefono, int curso, int campania);
    List<SeguimientoClientes> listaPendientesLlamada();
    BigDecimal totalDatosCRM(String estado);
    BigDecimal totalDatosCRMVendedor(String estado, Integer vendedor, Integer campania);
    List<DtoMatriculasCurso> listaInteresadosCursoCRM();
    List<DtoMatriculasCurso> listaEstadosContactoCursoCRM(String estado);
}

