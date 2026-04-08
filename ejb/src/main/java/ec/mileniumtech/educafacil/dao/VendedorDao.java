package ec.mileniumtech.educafacil.dao;

import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Vendedor;

public interface VendedorDao extends GenericoDao<Vendedor, Long> {
    List<Vendedor> listaDeVendedores();
}
