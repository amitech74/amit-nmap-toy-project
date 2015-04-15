package amit.nmapscans.models;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="HostScanInitiate",description="Initiation of Scanning")
public class HostScanInitiated {

	private Integer transactionId;

	private String status;

	private Link checkResults;

	public HostScanInitiated(Integer transactionId, String status) {
		this.transactionId = transactionId;
		this.status = status;
		this.checkResults = new Link("/nmap/" + transactionId);
	}

	@ApiModelProperty(value = "transactionId", required = true, position = 1, dataType = "Integer")
	public Integer getTransactionId() {
		return transactionId;
	}

	@ApiModelProperty(value = "status", required = true, position = 2, dataType = "String")
	public String getStatus() {
		return status;
	}

	@ApiModelProperty(value = "checkResults", required = true, position = 3, dataType = "Link")
	public Link getCheckResults() {
		return checkResults;
	}

}
