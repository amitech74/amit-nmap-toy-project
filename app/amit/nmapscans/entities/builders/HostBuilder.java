package amit.nmapscans.entities.builders;

import amit.nmapscans.entities.Host;
import org.joda.time.DateTime;

public class HostBuilder {

	private final Host host;

	private HostBuilder() {
		this.host = new Host();
	}

	public static HostBuilder newInstance() {
		return new HostBuilder();
	}

	public HostBuilder id(int id){
		host.setId(id);
		return this;
	}

	public HostBuilder name(String name){
		host.setName(name);
		return this;
	}

	public HostBuilder createdDate(){
		host.setCreatedDate(new DateTime().toDate());
		return this;
	}

	public Host build(){
		return host;
	}
}
