CREATE TABLE port_event (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  port_name varchar(255) NOT NULL,
  ship_name varchar(255) NOT NULL,
  ship_id varchar(30) NOT NULL,
  event_type VARCHAR(10) NOT NULL DEFAULT 'ENTER' CHECK (event_type IN ('ENTER', 'LEAVE')),
  updated_at BIGINT NOT NULL
)