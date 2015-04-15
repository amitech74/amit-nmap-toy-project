package amit.nmapscans.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "nmap_scans")
public class Scans implements BaseEntity  {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;


	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "hostId", nullable = false)
	private Host host;

	@Enumerated(EnumType.STRING)
	private ScanStatus status;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;

	private String data;


	public Host getHost() {
		return host;
	}

	public void setHost(Host host) {
		this.host = host;
	}

	public ScanStatus getStatus() {
		return status;
	}

	public void setStatus(ScanStatus status) {
		this.status = status;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public Date getCreatedDate() {
		return createdDate;
	}

	@Override
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Scans)) return false;

		Scans scans = (Scans) o;

		if (data != null ? !data.equals(scans.data) : scans.data != null) return false;
		if (!host.equals(scans.host)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = host.hashCode();
		result = 31 * result + (data != null ? data.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("Scans{");
		sb.append("host=").append(host);
		sb.append(", data='").append(data).append('\'');
		sb.append(", status=").append(status);
		sb.append('}');
		return sb.toString();
	}
}
