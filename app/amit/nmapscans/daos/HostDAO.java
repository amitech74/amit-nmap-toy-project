package amit.nmapscans.daos;

import amit.nmapscans.entities.Host;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

@Repository("HostDAO")
public class HostDAO extends AbstractBaseEntityDAO<Host> {

	public HostDAO() {
		this.type = Host.class;
	}

	public Host findByName(String name){

		TypedQuery<Host> query = em.createQuery("select h FROM Host h " +
						"WHERE h.name = :name " , Host.class);

		query.setParameter("name", name);
		try {
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

}
