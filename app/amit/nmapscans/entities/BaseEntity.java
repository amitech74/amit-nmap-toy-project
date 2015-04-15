package amit.nmapscans.entities;

import java.io.Serializable;
import java.util.Date;

public interface BaseEntity extends Serializable {
	int getId();
	void setId(int id);

	Date getCreatedDate();
	void setCreatedDate(Date createdDate);

}
