INSERT INTO biz_cmd (
	"id",
	"create_time",
	"last_update",
	"version",
	"biz_id",
	"biz_type",
	"server_ip",
	"fail_reason",
	"env_tag",
	"is_doing",
	"status",
	"retry_times",
	"max_retry_times",
	"next_exe_time",
	"enable_start_date",
	"enable_end_date"
)
VALUES
	(
		gen_random_uuid (),
		now(),
		now(),
		0,
		gen_random_uuid (),
		'TEST_ONE',
		NULL,
		NULL,
		'dev',
		'n',
		'I',
		0,
		10,
		now(),
		now(),
	now() + '1 day'
	);

INSERT INTO biz_lock (
	"id",
	"create_time",
	"last_update",
	"version",
	"lock_name",
	"env_tag",
	"status"
)
VALUES
	(
		gen_random_uuid (),
		now(),
		now(),
		0,
		'TEST_ONE',
		'dev',
		'ACTIVE'
	);
INSERT INTO biz_lock (
	"id",
	"create_time",
	"last_update",
	"version",
	"lock_name",
	"env_tag",
	"status"
)
VALUES
	(
		gen_random_uuid (),
		now(),
		now(),
		0,
		'TEST_TWO',
		'dev',
	'ACTIVE'
	);

INSERT INTO job_config (
	"id",
	"create_time",
	"last_update",
	"version",
	"job_name",
	"job_group",
	"job_class",
	"env_tag",
	"exec_date",
	"status",
	"is_loading",
	"job_cron_express",
	"job_time_zone",
	"job_exec_count",
	"retry_times",
	"last_exec_time",
	"next_exec_time"
)
VALUES
	(
		gen_random_uuid (),
		now(),
		now(),
		0,
		'MyTestJob1',
		'Test',
		'com.airwallex.batchjobs.task.jobwork.TestJobWorker',
		'dev',
		'2019-01-10',
		'NORMAL',
		'LOADING',
		'10 0/10 * * * ? *',
		'Asia/Hong_Kong',
		0, - 1,
		now(),
	now()
	);