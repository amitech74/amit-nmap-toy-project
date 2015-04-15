package amit.nmapscans.models;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="Port",description="Port information")
public class Port {

	Integer portId;
	String protocol;

	public Port(Integer portId, String protocol) {
		this.portId = portId;
		this.protocol = protocol;
	}

	@ApiModelProperty(value = "portId", required = true, position = 1, dataType = "Integer")
	public Integer getPortId() {
		return portId;
	}

	@ApiModelProperty(value = "protocol", required = true, position = 2, dataType = "String")
	public String getProtocol() {
		return protocol;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Port)) return false;

		Port port = (Port) o;

		if (!portId.equals(port.portId)) return false;
		if (!protocol.equals(port.protocol)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = portId.hashCode();
		result = 31 * result + protocol.hashCode();
		return result;
	}
}
