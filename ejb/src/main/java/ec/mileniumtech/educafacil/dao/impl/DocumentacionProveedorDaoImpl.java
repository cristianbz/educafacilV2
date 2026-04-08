/**
 * Este software esta protegido por derechos de autor CEIMSCAP
 */
package ec.mileniumtech.educafacil.dao.impl;

import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.DocumentacionProveedor;
import ec.mileniumtech.educafacil.dao.DocumentacionProveedorDao;
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
public class DocumentacionProveedorDaoImpl extends GenericoDaoImpl<DocumentacionProveedor, Long> implements DocumentacionProveedorDao{
	public DocumentacionProveedorDaoImpl() {
		
	}
	public DocumentacionProveedorDaoImpl(EntityManager em, Class<DocumentacionProveedor> entityClass) {
		super(em, entityClass);
		// TODO Auto-generated constructor stub
	}
	/**
	 * Agrega actualiza la documentacion de un proveedor
	 * @param documentacionProveedor
	 * @
	 * @
	 */
	@Override
	public void agregarActualizarDocumentacionProveedor(DocumentacionProveedor documentacionProveedor){
		try{
			if (documentacionProveedor.getDocpId()==0)
				getEntityManager().persist(documentacionProveedor);
			else
				getEntityManager().merge(documentacionProveedor);
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
	 * Devuelve la documentacion de un proveedor
	 * @param codigoProveedor
	 * @return
	 * @
	 */
	@Override
	public DocumentacionProveedor buscarDocumentacionPorProveedor(int codigoProveedor){
		try {
			Query query=getEntityManager().createNamedQuery(DocumentacionProveedor.DOCUMENTACION_POR_PROVEEDOR);
			query.setParameter("codigoProveedor", codigoProveedor);
			return (DocumentacionProveedor) query.getSingleResult();
		}catch(NoResultException e) {
			return null;
		}catch(Exception e) {
			throw new DaoException(e);
		}
	}
}

