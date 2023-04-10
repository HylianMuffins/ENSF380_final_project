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
(1, 'Slinky', 'fox');

DROP TABLE IF EXISTS TASKS;
CREATE TABLE TASKS (
	TaskID			int not null AUTO_INCREMENT,
	Description		varchar(50),
        Duration                int,
        MaxWindow               int,
	primary key (TaskID)
);

INSERT INTO TASKS (TaskID, Description, Duration, MaxWindow) VALUES
(9, 'Eyedrops', 45, 1),
(10, 'your mother', 45, 1),
(11, 'pain and suffering', 45, 1);

DROP TABLE IF EXISTS TREATMENTS;
CREATE TABLE TREATMENTS (
      	TreatmentID	int not null AUTO_INCREMENT,
	AnimalID	int not null,
	TaskID		int not null,
	StartHour	int not null,
	primary key (TreatmentID)
);

INSERT INTO TREATMENTS (AnimalID, TaskID, StartHour) VALUES
(1, 9, 12),
(1, 10, 12),
(1, 11, 12);