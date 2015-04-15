package amit.nmapscans.daos;

import amit.nmapscans.entities.Host;
import amit.nmapscans.entities.ScanStatus;
import amit.nmapscans.entities.Scans;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Repository("ScansDAO")
public class ScansDAO extends AbstractBaseEntityDAO<Scans> {

	public ScansDAO() {
		this.type = Scans.class;
	}

	public List<Scans> findAllScansForHost(String name){
		TypedQuery<Scans> query = em.createQuery("select s from Scans s " +
										"JOIN s.host h " +
										"WHERE h.name = :name ORDER BY s.updatedDate DESC" ,
						Scans.class);

		query.setParameter("name", name);

		return query.getResultList();
	}

	/**
	 * Retrieves the scan information of transaction Id passed in and also the previous successful scan right before it
	 * @param transactionId
	 * @return
	 */
	public List<Scans> findLastTwoScansForTransactionId(int transactionId){
		List<Scans> scanEntities = new ArrayList<>();
		final int lastTwoRecs=2;

		Scans currentEntity= find(transactionId);
		if(currentEntity == null)
			return scanEntities;

		scanEntities.add(currentEntity);

		//-- Find last successful entity that was successfully completed
		TypedQuery<Scans> query = em.createQuery("select s from Scans s " +
										"WHERE s.id < :id AND s.status = :completedStatus AND s.host = :hostEntity ORDER BY s.updatedDate DESC" ,
						Scans.class);

		query.setParameter("id", transactionId);
		query.setParameter("completedStatus", ScanStatus.Complete);
		query.setParameter("hostEntity", currentEntity.getHost());
		query.setMaxResults(1);

		try {
			scanEntities.add(query.getSingleResult());
		} catch (Exception e) {
			//-- there may not be any result and jpa barfs in that case
			//e.printStackTrace();
		}

		return scanEntities;
	}

}
