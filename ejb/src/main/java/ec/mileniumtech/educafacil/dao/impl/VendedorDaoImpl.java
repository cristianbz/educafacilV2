/**
 * Este software esta protegido por derechos de autor CEIMSCAP
 */
package ec.mileniumtech.educafacil.dao.impl;

import java.util.List;

import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Vendedor;
import ec.mileniumtech.educafacil.dao.VendedorDao;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

/**
*@author christian  Jun 15, 2024
*
*/
@Stateless
public class VendedorDaoImpl extends GenericoDaoImpl<Vendedor,Long> implements VendedorDao{
	public VendedorDaoImpl() {
		
	}
	public VendedorDaoImpl(EntityManager em, Class<Vendedor> entityClass) {
		super(em, entityClass);
		// TODO Auto-generated constructor stub
	}
	@Override
	@SuppressWarnings("unchecked")
	public List<Vendedor> listaDeVendedores(){
		try {
			Query query=getEntityManager().createNamedQuery(Vendedor.BUSCAR_VENDEDOR);
			return query.getResultList();
		}catch(NoResultException e) {
			return null;
		}catch(Exception e) {
			throw new DaoException(e);
		}
	}
}

