package amit.nmapscans.entities;

import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "nmap_hosts")
public class Host implements BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	private String name;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "host", cascade = CascadeType.ALL, orphanRemoval = true)
	@BatchSize(size = 20)
	private List<Scans> scans;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Scans> getScans() {
		return scans;
	}

	public void setScans(List<Scans> scans) {
		this.scans = scans;
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
		if (!(o instanceof Host)) return false;

		Host hosts = (Host) o;

		if (!name.equals(hosts.name)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("Hosts{");
		sb.append("id=").append(id);
		sb.append(", name='").append(name).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
