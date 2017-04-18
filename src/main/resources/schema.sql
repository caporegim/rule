DROP TABLE IF EXISTS TRANSACTION_RULE;
CREATE TABLE TRANSACTION_RULE (
  id INT NOT NULL IDENTITY,
  type INT,
  direction VARCHAR2(10),
  source INT,
  sum_condition VARCHAR2(10),
  sum BIGINT,
  action VARCHAR2(10) NOT NULL,
  balance_type INT,
  start_date DATETIME NOT NULL,
  finish_date DATETIME NOT NULL,
  created_date DATETIME NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY(type, direction, source, sum_condition, sum)
);

CREATE INDEX DATE_IDX ON TRANSACTION_RULE(start_date, finish_date);