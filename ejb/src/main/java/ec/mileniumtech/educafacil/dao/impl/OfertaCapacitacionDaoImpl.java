/**
 * Este software esta protegido por derechos de autor CEIMSCAP
 */
package ec.mileniumtech.educafacil.dao.impl;

import java.util.List;

import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Curso;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Especialidad;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.OfertaCapacitacion;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.OfertaCursos;
import ec.mileniumtech.educafacil.dao.OfertaCapacitacionDao;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.ApplicationScoped;
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
public class OfertaCapacitacionDaoImpl extends GenericoDaoImpl<OfertaCapacitacion, Long> implements OfertaCapacitacionDao{
	public OfertaCapacitacionDaoImpl() {
		
	}
	public OfertaCapacitacionDaoImpl(EntityManager em, Class<OfertaCapacitacion> entityClass) {
		super(em, entityClass);
		// TODO Auto-generated constructor stub
	}
	/**
	 * Devuelve una oferta de capacitacion en una area y especialidad
	 * @param area
	 * @param especialidad
	 * @param curso
	 * @return
	 * @
	 */
	@Override
	public OfertaCapacitacion buscarOfertaCapacitacion(int area,int especialidad,int curso){
		try {
			Query query=getEntityManager().createNamedQuery(OfertaCapacitacion.OFERTA_CAPACITACION);
			query.setParameter("area", area);
			query.setParameter("curso", curso);
			query.setParameter("especialidad", especialidad);
			return (OfertaCapacitacion) query.getSingleResult();
		}catch(NoResultException e) {
			return null;
		}catch(Exception e) {
			throw new DaoException(e);
		}
	}
	/**
	 * Devuelve las especialidades por area
	 * @param area
	 * @return
	 * @
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Especialidad> listaEspecialidadPorArea(int area){
		try {
			Query query=getEntityManager().createNamedQuery(OfertaCapacitacion.LISTA_ESPECIALIDAD_POR_AREA);
			query.setParameter("area", area);
			return query.getResultList();
		}catch(NoResultException e) {
			return null;
		}catch(Exception e) {
			throw new DaoException(e);
		}
	}
	/**
	 * Devuelve los cursos por area y especialidad
	 * @param area
	 * @param especialidad
	 * @return
	 * @
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Curso> listaCursosPorAreaEspecilidad(int area,int especialidad){
		try {
			Query query=getEntityManager().createNamedQuery(OfertaCapacitacion.LISTA_CURSOS_POR_AREA_ESPECIALIDAD);
			query.setParameter("area", area);
			query.setParameter("especialidad", especialidad);
			return query.getResultList();
		}catch(NoResultException e) {
			return null;
		}catch(Exception e) {
			throw new DaoException(e);
		}
	}
	/**
	 * Retorna una oferta de capacitacion por curso
	 * @param codigoCurso
	 * @return
	 * @
	 */
	@Override
	public OfertaCapacitacion buscarPorCurso(int codigoCurso){
		try {
			Query query=getEntityManager().createNamedQuery(OfertaCapacitacion.BUSCAR_POR_CURSO);
			return (OfertaCapacitacion) query.getSingleResult();
		}catch(NoResultException e) {
			return null;
		}catch(Exception e) {
			throw new DaoException(e);
		}
	}
	/**
	 * Carga todas las ofertas de capacitacion
	 * @return
	 * @
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<OfertaCapacitacion> listarOfertasCapacitacion(){
		try {
			Query query=getEntityManager().createNamedQuery(OfertaCapacitacion.CARGAR_TODAS_OFERTAS);
			return query.getResultList();
		}catch(NoResultException e) {
			return null;
		}catch(Exception e) {
			throw new DaoException(e);
		}
	}
	/**
	 * Agrega una oferta de capacitacion
	 * @param ofertaCapacitacion
	 * @param ofertaCursos
	 * @
	 * @
	 */
	@Override
	public void agregarOfertaCapacitacion(OfertaCapacitacion ofertaCapacitacion, OfertaCursos ofertaCursos){
		try {
			getEntityManager().persist(ofertaCapacitacion);
			ofertaCursos.setOfertaCapacitacion(ofertaCapacitacion);
			getEntityManager().persist(ofertaCursos);
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

