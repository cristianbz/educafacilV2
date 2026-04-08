/**
 * Este software esta protegido por derechos de autor CEIMSCAP
 */
package ec.mileniumtech.educafacil.dao.impl;

import java.util.List;

import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Proveedor;
import ec.mileniumtech.educafacil.dao.ProveedorDao;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;

/**
*@author christian  Jun 15, 2024
*
*/
@Stateless
public class ProveedorDaoImpl extends GenericoDaoImpl<Proveedor, Long> implements ProveedorDao{
	public ProveedorDaoImpl() {
		
	}
	public ProveedorDaoImpl(EntityManager em, Class<Proveedor> entityClass) {
		super(em, entityClass);
		// TODO Auto-generated constructor stub
	}
	/**
	 * Agrega actualiza un proveedor
	 * @param proveedor
	 * @
	 * @
	 */
	@Override
	public void agregarActualizarProveedor(Proveedor proveedor){
		try{			
			if (proveedor.getProvId()==null) {
				getEntityManager().persist(proveedor);
			}else {
				getEntityManager().merge(proveedor);
			}
		}catch(PersistenceException e){
			 Throwable t = e.getCause();
			    while ((t != null) && !(t instanceof ConstraintViolationException)) {
			        t = t.getCause();
			    }
			    if (t instanceof ConstraintViolationException) {
			    	throw new EntidadDuplicadaException(e);
			    }
			throw new DaoException(e);
		} 	catch (Exception e) {
			throw new DaoException(e);
		}
	}
	/**
	 * Devuelve la lista de proveedores
	 * @return
	 * @
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Proveedor> listaProveedores(){
		try {
			Query query=getEntityManager().createNamedQuery(Proveedor.LISTA_PROVEEDORES);
			return query.getResultList();
		}catch(NoResultException e) {
			return null;
		}catch(Exception e) {
			throw new DaoException(e);
		}
	}
	/**
	 * Valida el proveedor
	 * @param ruc
	 * @return
	 * @
	 */
	@Override
	public Proveedor validaProveedor(String ruc){
		try {
			Query query = getEntityManager().createNamedQuery(Proveedor.RUC_PROVEEDOR);
			query.setParameter("ruc", ruc);
			return (Proveedor) query.getSingleResult();
		}catch(NoResultException e) {
			return null;
		}catch(Exception e) {
			throw new DaoException(e);
		}
	}
}

