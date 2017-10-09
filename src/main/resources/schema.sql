/*
 Navicat Premium Data Transfer

 Source Server         : AWS - mariaDB Dhruv - cyberseed - root
 Source Server Type    : MariaDB
 Source Server Version : 50557
 Source Host           : 52.15.141.172
 Source Database       : appsecsage

 Target Server Type    : MariaDB
 Target Server Version : 50557
 File Encoding         : utf-8

 Date: 10/01/2017 11:26:54 AM
*/
CREATE DATABASE IF NOT EXISTS appsecsage;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `doctor`
-- ----------------------------
--DROP TABLE IF EXISTS `doctor`;
CREATE TABLE IF NOT EXISTS `doctor` (
  `username` varchar(255) NOT NULL,
  `pname` varchar(255) NOT NULL,
  `paddress` varchar(255) NOT NULL,
  `rphrase` varchar(255) NOT NULL,
  PRIMARY KEY (`username`),
  CONSTRAINT `FK_doctor_user` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `insurance_admin`
-- ----------------------------
--DROP TABLE IF EXISTS `insurance_admin`;
CREATE TABLE IF NOT EXISTS `insurance_admin` (
  `username` varchar(255) NOT NULL,
  `cname` varchar(255) NOT NULL,
  `caddress` varchar(255) NOT NULL,
  PRIMARY KEY (`username`),
  CONSTRAINT `FK_insurance_admin_user` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `medical_admin`
-- ----------------------------
--DROP TABLE IF EXISTS `medical_admin`;
CREATE TABLE IF NOT EXISTS `medical_admin` (
  `username` varchar(255) NOT NULL,
  `pname` varchar(255) NOT NULL,
  `paddress` varchar(255) NOT NULL,
  `adoctor` varchar(255) NOT NULL,
  `anurse` varchar(255) NOT NULL,
  PRIMARY KEY (`username`),
  CONSTRAINT `FK_medical_admin_user` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `nurse`
-- ----------------------------
--DROP TABLE IF EXISTS `nurse`;
CREATE TABLE IF NOT EXISTS `nurse` (
  `username` varchar(255) NOT NULL,
  `pname` varchar(255) NOT NULL,
  `paddress` varchar(255) NOT NULL,
  `adoctors` varchar(255) NOT NULL,
  PRIMARY KEY (`username`),
  CONSTRAINT `FK_nurse_user` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `patient`
-- ----------------------------
--DROP TABLE IF EXISTS `patient`;
CREATE TABLE IF NOT EXISTS `patient` (
  `username` varchar(255) NOT NULL,
  `dob` date NOT NULL,
  `ssn` int(9) NOT NULL,
  `address` varchar(255) NOT NULL,
  PRIMARY KEY (`username`),
  CONSTRAINT `FK_patient_user` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `record`
-- ----------------------------
--DROP TABLE IF EXISTS `record`;
CREATE TABLE IF NOT EXISTS `record` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `record_type` char(255) NOT NULL,
  `edit` varchar(255) NOT NULL,
  `view` varchar(255) NOT NULL,
  `owner` varchar(255) NOT NULL,
  `patient` varchar(255) NOT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_record_record_type` (`record_type`),
  KEY `FK_record_user` (`owner`),
  KEY `FK_record_user_2` (`patient`),
  CONSTRAINT `FK_record_user` FOREIGN KEY (`owner`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_record_user_2` FOREIGN KEY (`patient`) REFERENCES `patient` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `record_correspondence`
-- ----------------------------
--DROP TABLE IF EXISTS `record_correspondence`;
CREATE TABLE IF NOT EXISTS `record_correspondence` (
  `doctor` varchar(255),
  `id` int(255) NOT NULL,
  `note_id` int(255) NOT NULL AUTO_INCREMENT,
  `note_date` date,
  `note_text` VARCHAR(255),
  PRIMARY KEY (`note_id`),
  KEY `FK_record_correspondence_record` (`id`),
  KEY `FK_record_correspondence_doctor` (`doctor`),
  CONSTRAINT `FK_record_correspondence_doctor` FOREIGN KEY (`doctor`) REFERENCES `doctor` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_record_correspondence_record` FOREIGN KEY (`id`) REFERENCES `record` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `record_diagnosis`
-- ----------------------------
--DROP TABLE IF EXISTS `record_diagnosis`;
CREATE TABLE IF NOT EXISTS `record_diagnosis` (
  `id` int(255) NOT NULL,
  `date` date NOT NULL,
  `doctor` varchar(255) NOT NULL,
  `diagnosis` varchar(255) NOT NULL,
  KEY `FK_record_diagnosis_record` (`id`),
  KEY `FK_record_diagnosis_user` (`doctor`),
  CONSTRAINT `FK_record_diagnosis_record` FOREIGN KEY (`id`) REFERENCES `record` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_record_diagnosis_user` FOREIGN KEY (`doctor`) REFERENCES `doctor` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `record_doctor_exam`
-- ----------------------------
--DROP TABLE IF EXISTS `record_doctor_exam`;
CREATE TABLE IF NOT EXISTS `record_doctor_exam` (
  `id` int(255) NOT NULL,
  `doctor` varchar(255) NOT NULL,
  `date` date NOT NULL,
  `notes` varchar(255) NOT NULL,
  KEY `FK_record_doctor_exam_record` (`id`),
  KEY `FK_record_doctor_exam_user` (`doctor`),
  CONSTRAINT `FK_record_doctor_exam_record` FOREIGN KEY (`id`) REFERENCES `record` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_record_doctor_exam_user` FOREIGN KEY (`doctor`) REFERENCES `doctor` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `record_insurance`
-- ----------------------------
--DROP TABLE IF EXISTS `record_insurance`;
CREATE TABLE IF NOT EXISTS `record_insurance` (
  `id` int(255) NOT NULL,
  `date` date NOT NULL,
  `madmin` varchar(255) NOT NULL,
  `amount` Float NOT NULL,
  `status` varchar(255) NOT NULL,
  KEY `FK_record_insurance_record` (`id`),
  KEY `FK_record_insurance_user` (`madmin`),
  CONSTRAINT `FK_record_insurance_record` FOREIGN KEY (`id`) REFERENCES `record` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_record_insurance_user` FOREIGN KEY (`madmin`) REFERENCES `medical_admin` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `record_raw`
-- ----------------------------
--DROP TABLE IF EXISTS `record_raw`;
CREATE TABLE IF NOT EXISTS `record_raw` (
  `id` int(11) NOT NULL,
  `description` char(255) NOT NULL,
  `file` binary(255) NOT NULL,
  `length` int(3) NOT NULL,
  KEY `FK_record_raw_record` (`id`),
  CONSTRAINT `FK_record_raw_record` FOREIGN KEY (`id`) REFERENCES `record` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `record_testresult`
-- ----------------------------
--DROP TABLE IF EXISTS `record_testresult`;
CREATE TABLE IF NOT EXISTS `record_testresult` (
  `id` int(11) NOT NULL,
  `date` date NOT NULL,
  `doctor` varchar(255) NOT NULL,
  `lab` varchar(255) NOT NULL,
  `notes` varchar(255) NOT NULL,
  KEY `FK_record_testresult_record` (`id`),
  KEY `FK_record_testresult_user` (`doctor`),
  CONSTRAINT `FK_record_testresult_record` FOREIGN KEY (`id`) REFERENCES `record` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_record_testresult_user` FOREIGN KEY (`doctor`) REFERENCES `doctor` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--  Table structure for `user`
-- ----------------------------
--DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `password` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `Fname` varchar(255) NOT NULL,
  `Lname` varchar(255) NOT NULL,
  `roles` varchar(2550) DEFAULT NULL,
  `custom_permissions_to_add` varchar(2550) DEFAULT NULL,
  `custom_permissions_to_remove` varchar(2550) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `user_permissions_list`
-- ----------------------------
DROP TABLE IF EXISTS `user_permissions_list`;
CREATE TABLE `user_permissions_list` (
  `role` varchar(255) NOT NULL,
  `default_permissions` varchar(2550) NOT NULL,
  PRIMARY KEY (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `user_role_list`
-- ----------------------------
DROP TABLE IF EXISTS `user_role_list`;
CREATE TABLE `user_role_list` (
  `role` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `permissions_list`
-- ----------------------------
DROP TABLE IF EXISTS `permissions_list`;
CREATE TABLE `permissions_list` (
  `permission` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`permission`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

SET FOREIGN_KEY_CHECKS = 1;
