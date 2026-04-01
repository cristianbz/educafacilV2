package ec.mileniumtech.educafacil.dao;

import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Empresa;

public interface EmpresaDao extends GenericoDao<Empresa, Long> {
    List<Empresa> listaEmpresas() throws DaoException;
    void agregarEmpresa(Empresa empresa) throws DaoException, EntidadDuplicadaException;
}
