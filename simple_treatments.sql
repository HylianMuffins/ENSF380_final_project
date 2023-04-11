DROP DATABASE IF EXISTS EWR;
CREATE DATABASE EWR; 
USE EWR;

DROP TABLE IF EXISTS ANIMALS;
CREATE TABLE ANIMALS (
	AnimalID		int not null AUTO_INCREMENT,
        AnimalNickname		varchar(25),
	AnimalSpecies		varchar(25),
	primary key (AnimalID)
);

INSERT INTO ANIMALS (AnimalID, AnimalNickname, AnimalSpecies) VALUES
(1, 'Slinky', 'fox'),
(2, 'Slinky2', 'fox'),
(3, 'Slinky3', 'fox');

DROP TABLE IF EXISTS TASKS;
CREATE TABLE TASKS (
	TaskID			int not null AUTO_INCREMENT,
	Description		varchar(50),
        Duration                int,
        MaxWindow               int,
	primary key (TaskID)
);

INSERT INTO TASKS (TaskID, Description, Duration, MaxWindow) VALUES
(2, 'Rebandage leg wound', 20, 1),
(3, 'Apply burn ointment back', 30, 3),
(4, 'Administer antibiotics', 5, 2),
(5, 'Flush neck wound', 25, 1),
(9, 'Eyedrops', 40, 1),
(10, 'Inspect broken leg', 45, 1);

DROP TABLE IF EXISTS TREATMENTS;
CREATE TABLE TREATMENTS (
      	TreatmentID	int not null AUTO_INCREMENT,
	AnimalID	int not null,
	TaskID		int not null,
	StartHour	int not null,
	primary key (TreatmentID)
);

INSERT INTO TREATMENTS (AnimalID, TaskID, StartHour) VALUES
(1, 2, 12),
(1, 4, 12),
(1, 5, 13),
(1, 9, 13),
(2, 3, 16),
(4, 2, 16),
(2, 10, 20),
(3, 10, 20),
(4, 10, 20);