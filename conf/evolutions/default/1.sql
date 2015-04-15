# --- !Ups

CREATE TABLE IF NOT EXISTS nmap_hosts (
  id INT NOT NULL AUTO_INCREMENT COMMENT 'primary key - auto incremented',
  name VARCHAR(200) NOT NULL COMMENT 'hostname being scanned',
  createdDate datetime NOT NULL COMMENT 'The date the row was created',
  PRIMARY KEY (id),
  UNIQUE INDEX name_UNIQUE (name ASC)
) ENGINE=InnoDB DEFAULT  CHARSET=utf8;


# --- !Downs

DROP TABLE IF EXISTS nmap_hosts;
