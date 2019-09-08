CREATE TABLE project (
	projID int NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	projName VARCHAR(50),
	startDate DATE,
	endDate DATE
);

CREATE TABLE task (
	taskID int NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	projID int references schedule.project(projID),
	taskName VARCHAR(50),
	startDate DATE,
	endDate DATE
);

CREATE TABLE task_dependency (
	taskID int NOT NULL,
	taskDependentID int NOT NULL,
	CONSTRAINT taskDependentID_ref FOREIGN KEY (taskDependentID) REFERENCES schedule.task(taskID)
);

