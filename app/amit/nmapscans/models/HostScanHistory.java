package amit.nmapscans.models;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

@ApiModel(value="HostScanHistory",description="Scanning history of host")
public class HostScanHistory {

	String host;

	List<HostScanStatus> hostScanStatuses;

	public HostScanHistory(String host) {
		this.host = host;
		this.hostScanStatuses = new ArrayList<>();
	}

	@ApiModelProperty(value = "host", required = true, position = 1, dataType = "String")
	public String getHost() {
		return host;
	}

	@ApiModelProperty(value = "hostScanStatuses", required = true, position = 2)
	public List<HostScanStatus> getHostScanStatuses() {
		return hostScanStatuses;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Boolean add(HostScanStatus hss){
		return hostScanStatuses.add(hss);
	}
}
