package amit.nmapscans.models;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="HostScanStatus",description="Scanning Status")
public class HostScanStatus {

	String status;

	HostScanResult hostScanResult;

	public HostScanStatus(String status) {
		this.status = status;
	}

	@ApiModelProperty(value = "status", required = true, position = 1, dataType = "String")
	public String getStatus() {
		return status;
	}

	@ApiModelProperty(value = "hostScanResult", required = true, position = 2, dataType = "HostScanResult")
	public HostScanResult getHostScanResult() {
		return hostScanResult;
	}


	public void setStatus(String status) {
		this.status = status;
	}
	public void setHostScanResult(HostScanResult hostScanResult) {
		this.hostScanResult = hostScanResult;
	}
}
