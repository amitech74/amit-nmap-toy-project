package amit.nmapscans.models;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;
import java.util.Set;

@ApiModel(value="HostScanResult",description="Scanning Result")
public class HostScanResult {

	private String host;

	private Integer transactionId;

	private Long createdDate;

	private Long updatedDate;

	private String errorStatus;

	private Set<Port> ports;

	private Set<Port> addedPorts;

	private Set<Port> removedPorts;


	@ApiModelProperty(value = "host", required = true, position = 1, dataType = "String")
	public String getHost() {
		return host;
	}

	@ApiModelProperty(value = "transactionId", required = true, position = 2, dataType = "Integer")
	public Integer getTransactionId() {
		return transactionId;
	}

	@ApiModelProperty(value = "createdDate", required = true, position = 3, dataType = "Long")
	public Long getCreatedDate() {
		return createdDate;
	}

	@ApiModelProperty(value = "updatedDate", required = false, position = 4, dataType = "Long")
	public Long getUpdatedDate() {
		return updatedDate;
	}

	@ApiModelProperty(value = "errorStatus", required = false, position = 5, dataType = "String")
	public String getErrorStatus() {
		return errorStatus;
	}

	@ApiModelProperty(value = "ports", required = false, position = 6)
	public Set<Port> getPorts() {
		return ports;
	}

	@ApiModelProperty(value = "addedPorts", required = false, position = 7)
	public Set<Port> getAddedPorts() {
		return addedPorts;
	}

	@ApiModelProperty(value = "removedPorts", required = false, position = 8)
	public Set<Port> getRemovedPorts() {
		return removedPorts;
	}

	public void setErrorStatus(String errorStatus) {
		this.errorStatus = errorStatus;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}
	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}
	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}
	public void setPorts(Set<Port> ports) {
		this.ports = ports;
	}
	public void setAddedPorts(Set<Port> addedPorts) {
		this.addedPorts = addedPorts;
	}
	public void setRemovedPorts(Set<Port> removedPorts) {
		this.removedPorts = removedPorts;
	}
}
