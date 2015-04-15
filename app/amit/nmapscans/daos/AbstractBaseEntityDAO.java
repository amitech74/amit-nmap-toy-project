package amit.nmapscans.daos;


import amit.nmapscans.entities.BaseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class AbstractBaseEntityDAO<E extends BaseEntity> {

	@PersistenceContext
	protected EntityManager em;

	protected Class<E> type;

	public E find(Object id) {
		return em.find(type, id);
	}

	@Transactional(propagation = Propagation.NESTED)
	public E save(E entity) {
		entity = persistOrMerge(entity);
		em.flush();
		return entity;
	}

	@Transactional(propagation = Propagation.NESTED)
	protected E persistOrMerge(E entity) {
		if (entity.getId() == 0) {
			em.persist(entity);
		} else {
			entity = em.merge(entity);
		}

		return entity;
	}

	public E detach(E entity) {
		em.detach(entity);
		return entity;
	}

	public E refresh(E entity) {
		em.refresh(entity);
		return entity;
	}

}
