set foreign_key_checks = 0;

drop table if EXISTS `admin`;

CREATE TABLE IF NOT EXISTS `admin` (
  `iadmin` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `admin_id` varchar(10) NOT NULL,
  `admin_password` varchar(100) NOT NULL,
  `role` varchar(30) NOT NULL,
  `secret_key` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`iadmin`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


drop table if EXISTS `board`;

CREATE TABLE IF NOT EXISTS `board` (
  `iboard` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `del_yn` int(11) DEFAULT 0,
  `updated_at` datetime(6) DEFAULT NULL,
  `board_view` bigint(20) unsigned DEFAULT NULL,
  `ctnt` varchar(1000) NOT NULL,
  `importance` int(11) DEFAULT 0,
  `title` varchar(100) NOT NULL,
  `iadmin` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`iboard`),
  KEY `FK6jgf42ir4pkkyhu37q6598xfa` (`iadmin`),
  CONSTRAINT `FK6jgf42ir4pkkyhu37q6598xfa` FOREIGN KEY (`iadmin`) REFERENCES `admin` (`iadmin`)
  ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

drop table if EXISTS board_pics;

CREATE TABLE IF NOT EXISTS `board_pic` (
  `ipic` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `pic` varchar(255) DEFAULT NULL,
  `iboard` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`ipic`),
  KEY `FK7iba1tea69pi1pok0pxxrgkbc` (`iboard`),
  CONSTRAINT `FK7iba1tea69pi1pok0pxxrgkbc` FOREIGN KEY (`iboard`) REFERENCES `board` (`iboard`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

drop table if EXISTS lecture_name;

CREATE TABLE IF NOT EXISTS `lecture_name` (
  `ilecture_name` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `del_yn` int(11) DEFAULT NULL,
  `lecture_name` varchar(50) NOT NULL,
  `score` int(11) NOT NULL,
  PRIMARY KEY (`ilecture_name`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

drop table if EXISTS major;

CREATE TABLE IF NOT EXISTS `major` (
  `imajor` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `del_yn` int(11) DEFAULT NULL,
  `graduation_score` int(11) NOT NULL,
  `major_name` varchar(50) NOT NULL,
  `remarks` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`imajor`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

drop table if EXISTS professor;

CREATE TABLE IF NOT EXISTS `professor` (
  `iprofessor` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `del_yn` int(11) DEFAULT 0,
  `updated_at` datetime(6) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `birthdate` date DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `gender` enum('F','M') NOT NULL,
  `nm` varchar(15) DEFAULT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `pic` varchar(100) DEFAULT NULL,
  `professor_password` varchar(100) DEFAULT NULL,
  `role` varchar(30) NOT NULL DEFAULT 'ROLE_PROFESSOR',
  `secret_key` varchar(100) DEFAULT NULL,
  `imajor` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`iprofessor`),
  KEY `FKsyxpc9g73muxlbeeb9xhwyitc` (`imajor`),
  CONSTRAINT `FKsyxpc9g73muxlbeeb9xhwyitc` FOREIGN KEY (`imajor`) REFERENCES `major` (`imajor`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


drop table if EXISTS semester;

CREATE TABLE IF NOT EXISTS `semester` (
  `isemester` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `del_yn` int(11) DEFAULT 0,
  `lecture_apply_deadline` date NOT NULL,
  `semester` int(11) NOT NULL,
  `semester_end_date` date NOT NULL,
  `semester_str_date` date NOT NULL,
  `year` date DEFAULT NULL,
  PRIMARY KEY (`isemester`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;






drop table if EXISTS student;



CREATE TABLE IF NOT EXISTS `student` (
  `student_num` int(10) unsigned NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `del_yn` int(11) DEFAULT 0,
  `updated_at` datetime(6) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `birthdate` date NOT NULL,
  `email` varchar(50) DEFAULT NULL,
  `finished_yn` int(11) DEFAULT 1,
  `gender` enum('F','M') NOT NULL,
  `grade` int(11) NOT NULL DEFAULT 1,
  `nm` varchar(10) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `pic` varchar(100) DEFAULT NULL,
  `role` varchar(255) DEFAULT 'ROLE_STUDENT',
  `secret_key` varchar(255) DEFAULT NULL,
  `student_password` varchar(100) NOT NULL,
  `imajor` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`student_num`),
  KEY `FKhv44k3jgpp4p9y7gm0ibbg7my` (`imajor`),
  CONSTRAINT `FKhv44k3jgpp4p9y7gm0ibbg7my` FOREIGN KEY (`imajor`) REFERENCES `major` (`imajor`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



drop table if EXISTS student_semester_score;


CREATE TABLE IF NOT EXISTS `student_semester_score` (
  `grade` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `del_yn` int(11) DEFAULT 0,
  `updated_at` datetime(6) DEFAULT NULL,
  `avg_score` int(11) DEFAULT NULL,
  `rating` double DEFAULT NULL,
  `score` int(11) NOT NULL,
  `isemester` bigint(20) unsigned DEFAULT NULL,
  `student_num` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`grade`),
  KEY `FKnm29fr3n97phefi79j6pdxfqe` (`isemester`),
  KEY `FK4ouic34pd91tv6qbh1fxib9pn` (`student_num`),
  CONSTRAINT `FK4ouic34pd91tv6qbh1fxib9pn` FOREIGN KEY (`student_num`) REFERENCES `student` (`student_num`),
  CONSTRAINT `FKnm29fr3n97phefi79j6pdxfqe` FOREIGN KEY (`isemester`) REFERENCES `semester` (`isemester`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;




drop table if EXISTS lecture_applly;

CREATE TABLE IF NOT EXISTS `lecture_apply` (
  `ilecture` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `del_yn` int(11) DEFAULT 0,
  `updated_at` datetime(6) DEFAULT NULL,
  `attendance` int(11) DEFAULT NULL,
  `ctnt` varchar(1000) DEFAULT NULL,
  `final_examination` int(11) DEFAULT NULL,
  `grade_limit` int(11) NOT NULL,
  `lecture_end_date` date DEFAULT NULL,
  `lecture_max_people` int(11) NOT NULL,
  `midterm_examination` int(11) DEFAULT NULL,
  `opening_proceudres` int(11) DEFAULT NULL,
  `students_apply_deadline` date DEFAULT NULL,
  `textbook` varchar(20) NOT NULL,
  `ilecture_name` bigint(20) unsigned DEFAULT NULL,
  `ilecture_room` bigint(20) unsigned DEFAULT NULL,
  `iprofessor` bigint(20) unsigned DEFAULT NULL,
  `isemester` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`ilecture`),
  KEY `FK9sdrukq5n8if82johv1m9ubv0` (`ilecture_name`),
  KEY `FK4vbmm5jnp26kyopglnt8qm9w2` (`ilecture_room`),
  KEY `FKlpnw6xcux5a5fqt9bxnkh7kx9` (`iprofessor`),
  KEY `FKpe3uhmpf6vcfr4s4goycwuk8x` (`isemester`),
  CONSTRAINT `FK4vbmm5jnp26kyopglnt8qm9w2` FOREIGN KEY (`ilecture_room`) REFERENCES `lecture_room` (`ilecture_room`),
  CONSTRAINT `FK9sdrukq5n8if82johv1m9ubv0` FOREIGN KEY (`ilecture_name`) REFERENCES `lecture_name` (`ilecture_name`),
  CONSTRAINT `FKlpnw6xcux5a5fqt9bxnkh7kx9` FOREIGN KEY (`iprofessor`) REFERENCES `professor` (`iprofessor`),
  CONSTRAINT `FKpe3uhmpf6vcfr4s4goycwuk8x` FOREIGN KEY (`isemester`) REFERENCES `semester` (`isemester`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

drop table if EXISTS lecture_day_week;

CREATE TABLE IF NOT EXISTS lecture_day_week (
  `ilecture` bigint(20) unsigned DEFAULT NULL COMMENT '강의신청pk',
  `day_week` int(11) NOT NULL DEFAULT 0 COMMENT '강의요일',
  `del_yn` int(10) unsigned DEFAULT 0 COMMENT '삭제여부',
  KEY `ilecture` (`ilecture`),
  CONSTRAINT `lecture_day_week_ibfk_1` FOREIGN KEY (`ilecture`) REFERENCES `lecture_applly` (`ilecture`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='강의 요일';

drop table if EXISTS lecture_student;

CREATE TABLE IF NOT EXISTS `lecture_student` (
  `ilecture_student` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `del_yn` int(11) DEFAULT 0,
  `updated_at` datetime(6) DEFAULT NULL,
  `attendance` int(11) DEFAULT 0,
  `correction_at` date DEFAULT NULL,
  `final_examination` int(11) DEFAULT 0,
  `finished_at` date DEFAULT NULL,
  `finished_yn` int(11) NOT NULL DEFAULT 0,
  `midterm_examination` int(11) DEFAULT 0,
  `total_score` int(11) DEFAULT 0,
  `ilecture` bigint(20) unsigned DEFAULT NULL,
  `student_num` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`ilecture_student`),
  KEY `FKekictuolw5iy4e1vexudpfdfu` (`ilecture`),
  KEY `FKjxoe4tt8wypbobjp5uxerj6is` (`student_num`),
  CONSTRAINT `FKekictuolw5iy4e1vexudpfdfu` FOREIGN KEY (`ilecture`) REFERENCES `lecture_apply` (`ilecture`),
  CONSTRAINT `FKjxoe4tt8wypbobjp5uxerj6is` FOREIGN KEY (`student_num`) REFERENCES `student` (`student_num`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

drop table if EXISTS lecture_schedule;

CREATE TABLE IF NOT EXISTS `lecture_schedule` (
  `ilecture` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `day_week` int(11) NOT NULL,
  `del_yn` int(11) DEFAULT 0,
  `lecture_end_time` time(6) NOT NULL,
  `lecture_str_time` time(6) NOT NULL,
  `isemester` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`ilecture`),
  KEY `FK4o2grjgf44bmuy9trsn208ttt` (`isemester`),
  CONSTRAINT `FK4o2grjgf44bmuy9trsn208ttt` FOREIGN KEY (`isemester`) REFERENCES `semester` (`isemester`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

drop table if EXISTS lecture_room;

CREATE TABLE IF NOT EXISTS `lecture_room` (
  `ilecture_room` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `del_yn` int(11) DEFAULT 0,
  `updated_at` datetime(6) DEFAULT NULL,
  `building_name` varchar(50) NOT NULL,
  `lecture_room_name` varchar(50) NOT NULL,
  `max_capacity` int(11) NOT NULL,
  PRIMARY KEY (`ilecture_room`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

drop table if EXISTS leture_condition;

CREATE TABLE IF NOT EXISTS `lecture_condition` (
  `icodition` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `del_yn` int(11) DEFAULT 0,
  `updated_at` datetime(6) DEFAULT NULL,
  `return_ctnt` varchar(255) NOT NULL,
  `return_date` datetime(6) DEFAULT NULL,
  `ilecture` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`icodition`),
  KEY `FKeicgahvl7iqet6rk4afj7wmc0` (`ilecture`),
  CONSTRAINT `FKeicgahvl7iqet6rk4afj7wmc0` FOREIGN KEY (`ilecture`) REFERENCES `lecture_apply` (`ilecture`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

set foreign_key_checks = 1;