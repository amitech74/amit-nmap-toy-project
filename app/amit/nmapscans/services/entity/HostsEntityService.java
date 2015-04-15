package amit.nmapscans.services.entity;

import amit.nmapscans.daos.HostDAO;
import amit.nmapscans.entities.Host;
import amit.nmapscans.entities.builders.HostBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("HostsEntityService")
public class HostsEntityService {

	@Autowired
	private HostDAO hostDAO;

	/**
	 * Add host if it doesn't exist already.  A race condition can be encountered, if
	 * someone else has already added this host.  Handles exception internally.
	 * @param name
	 */
	public Host addHost(String name){
		Host host = hostDAO.findByName(name);
		if(host == null){
			host = HostBuilder.newInstance().name(name).createdDate().build();
			host = hostDAO.save(host);
		}

		return host;
	}

	public Host findByName(String name) {
		return hostDAO.findByName(name);
	}

}
