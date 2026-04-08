/**
 * Este software esta protegido por derechos de autor CEIMSCAP
 */
package ec.mileniumtech.educafacil.dao.impl;

import java.util.List;

import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.DetalleEvaluaCurso;
import ec.mileniumtech.educafacil.dao.DetalleEvaluaCursoDao;
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
public class DetalleEvaluaCursoDaoImpl extends GenericoDaoImpl<DetalleEvaluaCurso, Long> implements DetalleEvaluaCursoDao{
	public DetalleEvaluaCursoDaoImpl() {
		
	}
	public DetalleEvaluaCursoDaoImpl(EntityManager em, Class<DetalleEvaluaCurso> entityClass) {
		super(em, entityClass);
		// TODO Auto-generated constructor stub
	}
	@Override
	@SuppressWarnings("unchecked")
	public List<DetalleEvaluaCurso> listaDeDetallesDeEvaluacionDeCursos(){
		try {
			Query query=getEntityManager().createNamedQuery(DetalleEvaluaCurso.CARGAR_DETALLE_EVALUACION);
			return query.getResultList();
		}catch(NoResultException e) {
			return null;
		}catch(Exception e) {
			throw new DaoException(e);
		}
	}
	
	@Override
	public void guardarEncuesta(DetalleEvaluaCurso detalle){
		try{
			if (detalle.getDevcId() == null)
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
}

