/*==============================================================*/
/* DBMS name:      MySQL 5.7                                    */
/* Created on:     21.04.2018 23:04:09                          */
/*==============================================================*/


DROP DATABASE IF EXISTS Hospital;
CREATE DATABASE Hospital
  DEFAULT CHARACTER SET utf8mb4;
USE Hospital;
/*==============================================================*/
/* Table: Assignment                                            */
/*==============================================================*/
CREATE TABLE Diagnose
(
  id                   BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  code                 VARCHAR(10) NOT NULL UNIQUE,
  name                 VARCHAR(256) NOT NULL,
  description          VARCHAR(1024)
);

/*==============================================================*/
/* Table: Assignment                                            */
/*==============================================================*/
CREATE TABLE Assignment
(
   id                   BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   assignment_type_id   BIGINT NOT NULL,
   examination_id       BIGINT NOT NULL,
   executor_id          BIGINT NOT NULL,
   description          VARCHAR (1024) NOT NULL,
   start_date           DATE NOT NULL,
   end_date             DATE NOT NULL
);

/*==============================================================*/
/* Table: Hospitalization                                           */
/*==============================================================*/
CREATE TABLE Hospitalization
(
  id                   BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY
);

/*==============================================================*/
/* Table: AssignmentType                                        */
/*==============================================================*/
create table Assignment_Type
(
   id                       BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   name                     VARCHAR(30) NOT NULL UNIQUE,
   hospitalization_required BOOLEAN
);

/*==============================================================*/
/* Table: Examination                                    */
/*==============================================================*/
create table Examination
(
   id                       BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   doctor_id                BIGINT NOT NULL,
   patient_id               BIGINT NOT NULL,
   comment                  VARCHAR(1024),
   hospitalization_id       BIGINT NULL,
   date                     DATE NOT NULL,
   hospitalization_relation INT DEFAULT 0
);

/*==============================================================*/
/* Table: MedicalExamination                                    */
/*==============================================================*/
create table Examination_To_Diagnose
(
  examination_id      BIGINT NOT NULL,
  diagnose_id         BIGINT NOT NULL,
  primary key         (diagnose_id, examination_id)
);

/*==============================================================*/
/* Table: Role                                                  */
/*==============================================================*/
create table Role
(
   id                   BIGINT NOT NULL AUTO_INCREMENT,
   name                 VARCHAR(30) NOT NULL UNIQUE,
   primary key (id)
);

/*==============================================================*/
/* Table: RoleAllowedAssignmentType                             */
/*==============================================================*/
create table Role_To_Assignment_Type
(
   role_id              BIGINT NOT NULL,
   assignment_type_id   BIGINT NOT NULL,
   primary key          (assignment_type_id, role_id)
);

/*==============================================================*/
/* Table: User                                                  */
/*==============================================================*/
create table User
(
   id                   BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   login                VARCHAR(30) NOT NULL UNIQUE,
   password             VARCHAR(32) NOT NULL,
   first_name           VARCHAR(30) NOT NULL,
   last_name            VARCHAR(40) NOT NULL,
   middle_name          VARCHAR(40),
   date_of_birth        DATE NOT NULL,
   gender               TINYINT,
   passport_number      CHAR(8) NOT NULL UNIQUE,
   email                VARCHAR(50),
   phone                VARCHAR(20),
   address              VARCHAR(200),
   language             CHAR(2) NOT NULL,
   items_per_page       INT NOT NULL,
   photo_id             BIGINT,
   hospitalized         BOOLEAN DEFAULT FALSE
);

/*==============================================================*/
/* Table: UserToRoles                                           */
/*==============================================================*/
create table User_To_Role
(
   user_id         BIGINT NOT NULL,
   role_id         BIGINT NOT NULL,
   primary key     (user_id, role_id)
);

create table Photo
(
  id               BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name             VARCHAR(50),
  content          MEDIUMBLOB NULL
);

/*==============================================================*/
/* Foreign Keys                                          */
/*==============================================================*/
ALTER TABLE Assignment ADD CONSTRAINT FK_ASSIGNMENT_EXECUTOR_ID FOREIGN KEY (executor_id)
      REFERENCES User (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE Assignment ADD CONSTRAINT FK_ASSIGNMENT_EXAMINATION_ID FOREIGN KEY (examination_id)
      REFERENCES Examination (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE Assignment ADD CONSTRAINT FK_ASSIGNMENT_ASSIGNMENT_TYPE_ID FOREIGN KEY (assignment_type_id)
      REFERENCES Assignment_Type (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE Examination ADD CONSTRAINT FK_EXAMINATION_PATIENT_ID FOREIGN KEY (patient_id)
      REFERENCES User (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE Examination ADD CONSTRAINT FK_EXAMINATION_DOCTOR_ID FOREIGN KEY (doctor_id)
      REFERENCES User (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE Examination ADD CONSTRAINT FK_EXAMINATION_HOSPITALIZATION_ID FOREIGN KEY (hospitalization_id)
      REFERENCES Hospitalization (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE Role_To_Assignment_Type ADD CONSTRAINT FK_RAAT_ASSIGNMENT_TYPE_ID FOREIGN KEY (assignment_type_id)
      REFERENCES Assignment_Type (id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE Role_To_Assignment_Type ADD CONSTRAINT FK_RAAT_ROLE_ID FOREIGN KEY (role_id)
      REFERENCES Role (id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE User_To_Role ADD CONSTRAINT FK_USER_TO_ROLES_ROLE_ID FOREIGN KEY (role_id)
      REFERENCES Role (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE User_To_Role ADD CONSTRAINT FK_USER_TO_ROLES_USER_ID FOREIGN KEY (user_id)
      REFERENCES User (id) ON DELETE CASCADE ON UPDATE RESTRICT;

ALTER TABLE Examination_To_Diagnose ADD CONSTRAINT FK_DIAGNOSE_TO_EXAMINATION_EXAMINATION_ID
      FOREIGN KEY (examination_id)
      REFERENCES Examination (id) ON DELETE CASCADE ON UPDATE RESTRICT;

ALTER TABLE Examination_To_Diagnose ADD CONSTRAINT FK_DIAGNOSE_TO_EXAMINATION_DIAGNOSE_ID
      FOREIGN KEY (diagnose_id)
      REFERENCES Diagnose (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE User ADD CONSTRAINT FK_USER_PHOTO_ID FOREIGN KEY (photo_id)
      REFERENCES Photo (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

CREATE VIEW Assignment_Full AS
  SELECT assignment.*, patient_id, doctor_id FROM Assignment JOIN Examination
      ON assignment.examination_id = examination.id;

CREATE VIEW Hospitalization_Full AS
  SELECT
  h.id AS id,
  es.patient_id AS patient_id,
  es.id AS initial_examination_id,
  es.doctor_id AS accepted_doctor_id,
  es.date AS start_date,
  ee.id AS discharge_examination_id,
  ee.doctor_id AS discharged_doctor_id,
  ee.date AS end_date
  FROM Hospitalization h JOIN Examination es ON h.id = es.hospitalization_id
  LEFT JOIN (SELECT
    id, doctor_id, date, hospitalization_id
    FROM Examination WHERE hospitalization_relation = 3) AS ee
  ON h.id = ee.hospitalization_id
    WHERE es.hospitalization_relation = 1;

INSERT INTO Role (name) VALUES ("Пациент");
INSERT INTO Role (name) VALUES ("Медсестра");
INSERT INTO Role (name) VALUES ("Врач");
INSERT INTO Role (name) VALUES ("Администратор");

INSERT INTO Assignment_Type (name, hospitalization_required) VALUES ('Лекарство', false);
INSERT INTO Assignment_Type (name, hospitalization_required) VALUES ('Процедура', true);
INSERT INTO Assignment_Type (name, hospitalization_required) VALUES ('Операция', true);

INSERT INTO Role_To_Assignment_Type (role_id, assignment_type_id) VALUES (1, 1);
INSERT INTO Role_To_Assignment_Type (role_id, assignment_type_id) VALUES (2, 1);
INSERT INTO Role_To_Assignment_Type (role_id, assignment_type_id) VALUES (2, 2);
INSERT INTO Role_To_Assignment_Type (role_id, assignment_type_id) VALUES (3, 1);
INSERT INTO Role_To_Assignment_Type (role_id, assignment_type_id) VALUES (3, 2);
INSERT INTO Role_To_Assignment_Type (role_id, assignment_type_id) VALUES (3, 3);

INSERT INTO user(login, password, first_name, last_name, middle_name, date_of_birth, gender, passport_number, email, phone, address, language, items_per_page, photo_id)
  VALUES ('anton', '5c9f9903627f3b3b55d776a055189e44', 'Антон', 'Евтух', 'Николаевич', '1991-02-25', 0, 'ME814742', '', '', '', 'ru', 10, NULL);
INSERT INTO User_To_Role(user_id, role_id) VALUES (1,1);
INSERT INTO User_To_Role(user_id, role_id) VALUES (1,2);
INSERT INTO User_To_Role(user_id, role_id) VALUES (1,3);
INSERT INTO User_To_Role(user_id, role_id) VALUES (1,4);

INSERT INTO user(login, password, first_name, last_name, middle_name, date_of_birth, gender, passport_number, email, phone, address, language, items_per_page, photo_id)
VALUES ('erna', '5c9f9903627f3b3b55d776a055189e44', 'Эрна', 'Флегель', '', '1911-07-11', 0, 'AA111111', '', '', '', 'en', 10, NULL);
INSERT INTO User_To_Role(user_id, role_id) VALUES (2,2);

INSERT INTO user(login, password, first_name, last_name, middle_name, date_of_birth, gender, passport_number, email, phone, address, language, items_per_page, photo_id)
VALUES ('jessop', '5c9f9903627f3b3b55d776a055189e44', 'Вайолетт', 'Джессоп', 'Констанс', '1887-10-02', 0, 'BB222222', '', '', '', 'ru', 10, NULL);
INSERT INTO User_To_Role(user_id, role_id) VALUES (3,2);

INSERT INTO user(login, password, first_name, last_name, middle_name, date_of_birth, gender, passport_number, email, phone, address, language, items_per_page, photo_id)
VALUES ('nightingale', '5c9f9903627f3b3b55d776a055189e44', 'Флоренс', 'Найтингейл', '', '1820-05-12', 0, 'CC333333', '', '', '', 'en', 10, NULL);
INSERT INTO User_To_Role(user_id, role_id) VALUES (4,2);

INSERT INTO user(login, password, first_name, last_name, middle_name, date_of_birth, gender, passport_number, email, phone, address, language, items_per_page, photo_id)
VALUES ('mengele', '5c9f9903627f3b3b55d776a055189e44', 'Менгеле', 'Йозеф', '', '1911-03-16', 0, 'HH148814', '', '', '', 'en', 10, NULL);
INSERT INTO User_To_Role(user_id, role_id) VALUES (5,3);

INSERT INTO user(login, password, first_name, last_name, middle_name, date_of_birth, gender, passport_number, email, phone, address, language, items_per_page, photo_id)
VALUES ('isii', '5c9f9903627f3b3b55d776a055189e44', 'Сиро', 'Исии', '', '1892-06-25', 0, 'HH731731', '', '', '', 'en', 10, NULL);
INSERT INTO User_To_Role(user_id, role_id) VALUES (6,3);

INSERT INTO user(login, password, first_name, last_name, middle_name, date_of_birth, gender, passport_number, email, phone, address, language, items_per_page, photo_id)
VALUES ('pavlov', '5c9f9903627f3b3b55d776a055189e44', 'Иван', 'Павлов', 'Петрович', '1892-06-25', 0, 'DD444444', '', '', '', 'en', 10, NULL);
INSERT INTO User_To_Role(user_id, role_id) VALUES (7,3);

INSERT INTO user(login, password, first_name, last_name, middle_name, date_of_birth, gender, passport_number, email, phone, address, language, items_per_page, photo_id)
VALUES ('bleuler', '5c9f9903627f3b3b55d776a055189e44', 'Ойген', 'Блойлер', '', '1857-04-30', 0, 'EE555555', '', '', '', 'en', 10, NULL);
INSERT INTO User_To_Role(user_id, role_id) VALUES (8,3);

INSERT INTO Diagnose (code, name) values ("F10.0", "Острая интоксикация");
INSERT INTO Diagnose (code, name) values ("F10.1", "Пагубное употребление");
INSERT INTO Diagnose (code, name) values ("F10.2", "Синдром зависимости");
INSERT INTO Diagnose (code, name) values ("F10.3", "Абстинентное состояние");
INSERT INTO Diagnose (code, name) values ("F10.4", "Абстинентное состояние с делирием");
INSERT INTO Diagnose (code, name) values ("F10.5", "Психотическое расстройство");
INSERT INTO Diagnose (code, name) values ("F10.6", "Амнестический синдром");
INSERT INTO Diagnose (code, name) values ("F10.7", "Резидуальные и отсроченные психотические расстройства");
INSERT INTO Diagnose (code, name) values ("F10.8", "Другие психические расстройства и расстройства поведения");
INSERT INTO Diagnose (code, name) values ("F10.9", "Психическое расстройство и расстройство поведения неуточненное");
INSERT INTO Diagnose (code, name) values ("F11.0", "Острая интоксикация");
INSERT INTO Diagnose (code, name) values ("F11.1", "Пагубное употребление");
INSERT INTO Diagnose (code, name) values ("F11.2", "Синдром зависимости");
INSERT INTO Diagnose (code, name) values ("F11.3", "Абстинентное состояние");
INSERT INTO Diagnose (code, name) values ("F11.4", "Абстинентное состояние с делирием");
INSERT INTO Diagnose (code, name) values ("F11.5", "Психотическое расстройство");
INSERT INTO Diagnose (code, name) values ("F11.6", "Амнестический синдром");
INSERT INTO Diagnose (code, name) values ("F11.7", "Резидуальные и отсроченные психотические расстройства");
INSERT INTO Diagnose (code, name) values ("F11.8", "Другие психические расстройства и расстройства поведения");
INSERT INTO Diagnose (code, name) values ("F11.9", "Психическое расстройство и расстройство поведения неуточненное");
INSERT INTO Diagnose (code, name) values ("F12.0", "Острая интоксикация");
INSERT INTO Diagnose (code, name) values ("F12.1", "Пагубное употребление");
INSERT INTO Diagnose (code, name) values ("F12.2", "Синдром зависимости");
INSERT INTO Diagnose (code, name) values ("F12.3", "Абстинентное состояние");
INSERT INTO Diagnose (code, name) values ("F12.4", "Абстинентное состояние с делирием");
INSERT INTO Diagnose (code, name) values ("F12.5", "Психотическое расстройство");
INSERT INTO Diagnose (code, name) values ("F12.6", "Амнестический синдром");

