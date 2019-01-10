CREATE TABLE job_config (
  id                    uuid        NOT NULL,
  create_time           timestamp   NOT NULL,
  last_update           timestamp   NOT NULL,
  version               int4        NOT NULL,
  job_name        		  text 		    NOT NULL,
  job_group          	  text        NOT NULL,
  job_class         	  text 		    NOT NULL,
  env_tag				        varchar(16) NOT NULL,
  exec_date  			      date        NOT NULL,
  status          		  varchar(16) NOT NULL,
  is_loading        	  varchar(16)	NOT NULL,
  job_cron_express		  text		NOT NULL,
  job_time_zone			    text		NOT NULL,
  job_exec_count		    int4		NOT NULL,
  retry_times           int4  		NOT NULL,
  last_exec_time        timestamp,
  next_exec_time		    timestamp,
  CONSTRAINT job_config_pkey PRIMARY KEY(job_name, job_group, env_tag)
);

CREATE TABLE job_logs (
  id                    uuid        NOT NULL,
  create_time           timestamp   NOT NULL,
  last_update           timestamp   NOT NULL,
  version               int4        NOT NULL,
  job_id     			      uuid,
  job_name   			      text 		    NOT NULL,
  job_group          	  text        NOT NULL,
  env_tag               text        NOT NULL,
  exec_date  			      date,
  status     			      varchar(16),
  error_msg  			      text,
  start_time 			      timestamp,
  exec_times 			      int4,
  machine_ip 			      text,
  CONSTRAINT job_logs_pkey PRIMARY KEY(id)
);

CREATE INDEX idx_job_logs_job_id ON job_logs USING BTREE (job_id, exec_date);

create table biz_lock (
  id                    uuid        NOT NULL,
  create_time           timestamp   NOT NULL,
  last_update           timestamp   NOT NULL,
  version               int4        NOT NULL,
  lock_name  			      text 		NOT NULL,
  env_tag				        varchar(16) NOT NULL,
  status     			      varchar(16) NOT NULL,
  CONSTRAINT biz_lock_pkey PRIMARY KEY(lock_name, env_tag)
);

create table biz_cmd (
  id                    uuid        NOT NULL,
  create_time           timestamp   NOT NULL,
  last_update           timestamp   NOT NULL,
  version               int4        NOT NULL,
  biz_id            	  text 		    NOT NULL,
  biz_type          	  text,
  server_ip         	  text,
  fail_reason       	  text,
  env_tag           	  varchar(16) NOT NULL,
  is_doing          	  varchar(2)	NOT NULL,
  status            	  varchar(16) NOT NULL,
  exec_date  			      date,
  retry_times       	  int4,
  max_retry_times   	  int4,
  next_exe_time     	  timestamp,
  enable_start_date 	  timestamp,
  enable_end_date   	  timestamp,
  CONSTRAINT biz_cmd_pkey PRIMARY KEY(biz_id, exec_date)
);