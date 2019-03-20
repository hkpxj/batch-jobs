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
	"retry_times",
	"last_exec_time",
	"next_exec_time"
)
VALUES
	(
		'17953576-6f59-48db-8621-abe3920941b2',
		'2019-03-20 15:20:28.872774',
		'2019-03-20 15:35:54.118981',
		0,
		'MyTestJob',
		'Test',
		'com.airwallex.batchjobs.task.jobwork.TestJobWorker',
		'dev',
		'2019-03-20',
		'NORMAL',
		'LOADING',
		'0 0/1 * * * ? *',
		'Asia/Hong_Kong',
		10,
		'2019-03-20 15:35:00',
	'2019-03-20 15:37:00'
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
		'aaffe0d1-532a-4f1d-bdf2-83b88ee80a55',
		'2019-03-20 15:20:28.872774',
		'2019-03-20 15:35:54.118981',
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
		'd01b35f7-8ece-4ec7-ba99-462be444e20e',
		'2019-03-20 15:20:28.872774',
		'2019-03-20 15:35:54.118981',
		0,
		'TEST_TWO',
		'dev',
		'ACTIVE'
	);