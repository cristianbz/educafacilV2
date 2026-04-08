package ec.mileniumtech.educafacil.dao;

import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.DocumentacionProveedor;

public interface DocumentacionProveedorDao extends GenericoDao<DocumentacionProveedor, Long> {
    void agregarActualizarDocumentacionProveedor(DocumentacionProveedor documentacionProveedor);
    DocumentacionProveedor buscarDocumentacionPorProveedor(int codigoProveedor);
}

