package ec.mileniumtech.educafacil.dao;

import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Proveedor;

public interface ProveedorDao extends GenericoDao<Proveedor, Long> {
    void agregarActualizarProveedor(Proveedor proveedor) throws DaoException, EntidadDuplicadaException;
    List<Proveedor> listaProveedores() throws DaoException;
    Proveedor validaProveedor(String ruc) throws DaoException;
}
