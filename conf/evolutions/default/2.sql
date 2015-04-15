# --- !Ups

CREATE TABLE nmap_scans (
  id INT NOT NULL AUTO_INCREMENT COMMENT 'primary key - auto incremented. ',
  hostId INT NOT NULL,
  status VARCHAR(25) COMMENT 'Current Status of this scan - InProgress, Done',
  data TEXT NULL,
  createdDate datetime NOT NULL COMMENT 'The date the row was created',
  updatedDate datetime NULL DEFAULT NULL COMMENT 'The date the row was updated',
  PRIMARY KEY (id),
  KEY fk1_nmap_scans (hostId),
  CONSTRAINT fk1_nmap_scans FOREIGN KEY (hostId) REFERENCES nmap_hosts (id)
) ENGINE=InnoDB DEFAULT  CHARSET=utf8;

# --- !Downs

DROP TABLE IF EXISTS nmap_scans;
