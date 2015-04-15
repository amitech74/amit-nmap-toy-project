# --- !Ups

ALTER TABLE nmap_hosts ADD INDEX ix_nmap_hosts_name (name);

ALTER TABLE nmap_scans ADD INDEX ix_nmap_scans_status (status);

# --- !Downs

ALTER TABLE nmap_hosts DROP INDEX ix_nmap_hosts_name;

ALTER TABLE nmap_scans DROP INDEX ix_nmap_scans_status;
