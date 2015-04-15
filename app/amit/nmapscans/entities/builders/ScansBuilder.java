package amit.nmapscans.entities.builders;

import amit.nmapscans.entities.Host;
import amit.nmapscans.entities.ScanStatus;
import amit.nmapscans.entities.Scans;
import org.joda.time.DateTime;

public class ScansBuilder {

	private final Scans scans;

	private ScansBuilder(){
		this.scans = new Scans();
	}

	private ScansBuilder(Scans scans){
		this.scans = scans;
	}

	public static ScansBuilder newInstance() {
		return new ScansBuilder();
	}

	public static ScansBuilder updateInstance(Scans scans) {
		return new ScansBuilder(scans);
	}

	public ScansBuilder id(int id){
		scans.setId(id);
		return this;
	}

	public ScansBuilder host(Host host){
		scans.setHost(host);
		return this;
	}

	public ScansBuilder scanStatus(ScanStatus scanStatus){
		scans.setStatus(scanStatus);
		return this;
	}

	public ScansBuilder data(String data){
		scans.setData(data);
		return this;
	}

	public ScansBuilder createdDate(){
		scans.setCreatedDate(new DateTime().toDate());
		return this;
	}

	public ScansBuilder updatedDate(){
		scans.setUpdatedDate(new DateTime().toDate());
		return this;
	}

	public Scans build(){
		return scans;
	}
}
