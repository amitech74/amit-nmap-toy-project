package amit.nmapscans.services.entity;

import amit.nmapscans.daos.ScansDAO;
import amit.nmapscans.entities.Scans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ScansEntityService")
public class ScansEntityService {

	@Autowired
	ScansDAO scansDAO;

	public List<Scans> findAllScansForHost(String name) {
		return scansDAO.findAllScansForHost(name);
	}

	public Scans find(Object id) {
		return scansDAO.find(id);
	}

	public List<Scans> findLastTwoScansForTransactionId(int transactionId) {
		return scansDAO.findLastTwoScansForTransactionId(transactionId);
	}

	public Scans save(Scans entity) {
		return scansDAO.save(entity);
	}
}
