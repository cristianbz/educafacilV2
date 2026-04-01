package ec.mileniumtech.educafacil.dao.impl;

import java.util.List;
import java.util.Optional;

import ec.mileniumtech.educafacil.dao.GenericoDao;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import lombok.Getter;
import lombok.Setter;

/**
 * @author [ Christian Baez ]cbaez
 *
 */
@Stateless
@Getter
@Setter
public class GenericoDaoImpl <T,K> implements GenericoDao<T,K> {
	@PersistenceContext
	private EntityManager entityManager;
    private Class<T> entityClass;

    public GenericoDaoImpl(EntityManager em, Class<T> entityClass) {
        this.entityManager = em;
        this.entityClass = entityClass;
    }
    public GenericoDaoImpl() {
    	
    }

	@Override
	public T guardar(T entity) {
		try {
			entityManager.persist(entity);
			return entity;
		} catch (PersistenceException e) {
			throw new DaoException("Error al persistir la entidad: " + (entityClass != null ? entityClass.getSimpleName() : "Desconocido"), e);
		}
	}

	@Override
	public void remover(T entity) {
		try {
			entityManager.remove(entityManager.merge(entity));
		} catch (PersistenceException e) {
			throw new DaoException("Error al eliminar la entidad: " + (entityClass != null ? entityClass.getSimpleName() : "Desconocido"), e);
		}
	}

	@Override
	public void detach(T entity) {
		entityManager.detach(entity);
	}

	@Override
	public T actualizar(T entity) {		
		try {
			entityManager.merge(entity);
			entityManager.flush();
			return entity;
		} catch (PersistenceException e) {
			throw new DaoException("Error al actualizar la entidad: " + (entityClass != null ? entityClass.getSimpleName() : "Desconocido"), e);
		}
	}	

	public void cerrarConexion() {
		entityManager.getEntityManagerFactory().close();
	}

	@Override
	public boolean validarCadenaNula(String label) {
		boolean isNull = true;
		if (null != label && !"".equals(label)) {
			isNull = false;
		}
		return isNull;
	}

	@Override
	public Optional<T> findById(K id) {
		try {
			return Optional.ofNullable(entityManager.find(entityClass, id));
		} catch (PersistenceException e) {
			throw new DaoException("Error al buscar por ID: " + id, e);
		}
	}

	@Override
	public List<T> findAll() {
		try {
			String entityName = entityClass != null ? entityClass.getSimpleName() : "";
			return getEntityManager().createQuery("SELECT E FROM "+ entityName + " E", entityClass).getResultList();
		} catch (PersistenceException e) {
			throw new DaoException("Error al listar todas las entidades", e);
		}
	}
	
}
