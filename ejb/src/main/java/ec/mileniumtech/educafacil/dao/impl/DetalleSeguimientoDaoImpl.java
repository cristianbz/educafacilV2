/**
 * Este software esta protegido por derechos de autor CEIMSCAP
 */
package ec.mileniumtech.educafacil.dao.impl;

import java.util.List;

import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.DetalleSeguimiento;
import ec.mileniumtech.educafacil.dao.DetalleSeguimientoDao;
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
public class DetalleSeguimientoDaoImpl extends GenericoDaoImpl<DetalleSeguimiento, Long> implements DetalleSeguimientoDao {
	public DetalleSeguimientoDaoImpl() {
		
	}
	public DetalleSeguimientoDaoImpl(EntityManager em, Class<DetalleSeguimiento> entityClass) {
		super(em, entityClass);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void agregarDetalle(DetalleSeguimiento detalle) {
		try{
			if (detalle.getDsegId() == 0)
				getEntityManager().persist(detalle);
			else
				getEntityManager().merge(detalle);
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

	@Override
	@SuppressWarnings("unchecked")
	public List<DetalleSeguimiento> listaDetalle(Integer seguimiento){
		try {
			Query query=getEntityManager().createNamedQuery(DetalleSeguimiento.LISTA_DETALLE);
			query.setParameter("seguimiento", seguimiento);
			return query.getResultList();
		}catch(NoResultException e) {
			return null;
		}catch(Exception e) {
			throw new DaoException(e);
		}
	}
}

