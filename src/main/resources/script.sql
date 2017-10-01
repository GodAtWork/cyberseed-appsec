-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.2.8-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for test1
CREATE DATABASE IF NOT EXISTS `test1` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `test1`;

-- Dumping structure for table test1.doctor
CREATE TABLE IF NOT EXISTS `doctor` (
    `username` varchar(255) NOT NULL,
    `pname` varchar(255) NOT NULL,
    `paddress` varchar(255) NOT NULL,
    `rphrase` varchar(255) NOT NULL,
    PRIMARY KEY (`username`),
    CONSTRAINT `FK_doctor_user` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table test1.doctor: ~1 rows (approximately)
/*!40000 ALTER TABLE `doctor` DISABLE KEYS */;
INSERT INTO `doctor` (`username`, `pname`, `paddress`, `rphrase`) VALUES
    ('doctor', 'doctor', 'doctor', 'doctor');
/*!40000 ALTER TABLE `doctor` ENABLE KEYS */;

-- Dumping structure for table test1.insurance_admin
CREATE TABLE IF NOT EXISTS `insurance_admin` (
    `username` varchar(255) NOT NULL,
    `cname` varchar(255) NOT NULL,
    `caddress` varchar(255) NOT NULL,
    PRIMARY KEY (`username`),
    CONSTRAINT `FK_insurance_admin_user` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table test1.insurance_admin: ~1 rows (approximately)
/*!40000 ALTER TABLE `insurance_admin` DISABLE KEYS */;
INSERT INTO `insurance_admin` (`username`, `cname`, `caddress`) VALUES
    ('iad', 'iad', 'iad');
/*!40000 ALTER TABLE `insurance_admin` ENABLE KEYS */;

-- Dumping structure for table test1.medical_admin
CREATE TABLE IF NOT EXISTS `medical_admin` (
    `username` varchar(255) NOT NULL,
    `pname` varchar(255) NOT NULL,
    `paddress` varchar(255) NOT NULL,
    `adoctor` varchar(255) NOT NULL,
    `anurse` varchar(255) NOT NULL,
    PRIMARY KEY (`username`),
    KEY `FK_medical_admin_user_2` (`anurse`),
    KEY `FK_medical_admin_user_3` (`adoctor`),
    CONSTRAINT `FK_medical_admin_user` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `FK_medical_admin_user_2` FOREIGN KEY (`anurse`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `FK_medical_admin_user_3` FOREIGN KEY (`adoctor`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table test1.medical_admin: ~1 rows (approximately)
/*!40000 ALTER TABLE `medical_admin` DISABLE KEYS */;
INSERT INTO `medical_admin` (`username`, `pname`, `paddress`, `adoctor`, `anurse`) VALUES
    ('medad', 'medad', 'medad', 'doctor', 'nurse');
/*!40000 ALTER TABLE `medical_admin` ENABLE KEYS */;

-- Dumping structure for table test1.nurse
CREATE TABLE IF NOT EXISTS `nurse` (
    `username` varchar(255) NOT NULL,
    `pname` varchar(255) NOT NULL,
    `paddress` varchar(255) NOT NULL,
    `adoctor` varchar(255) NOT NULL,
    PRIMARY KEY (`username`),
    KEY `FK_nurse_user_2` (`adoctor`),
    CONSTRAINT `FK_nurse_user` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `FK_nurse_user_2` FOREIGN KEY (`adoctor`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table test1.nurse: ~1 rows (approximately)
/*!40000 ALTER TABLE `nurse` DISABLE KEYS */;
INSERT INTO `nurse` (`username`, `pname`, `paddress`, `adoctor`) VALUES
    ('nurse', 'nurse', 'nurse', 'doctor');
/*!40000 ALTER TABLE `nurse` ENABLE KEYS */;

-- Dumping structure for table test1.patient
CREATE TABLE IF NOT EXISTS `patient` (
    `username` varchar(255) NOT NULL,
    `dob` date NOT NULL,
    `ssn` int(9) NOT NULL,
    `address` varchar(255) NOT NULL,
    PRIMARY KEY (`username`),
    CONSTRAINT `FK_patient_user` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table test1.patient: ~1 rows (approximately)
/*!40000 ALTER TABLE `patient` DISABLE KEYS */;
INSERT INTO `patient` (`username`, `dob`, `ssn`, `address`) VALUES
    ('patient', '2017-09-30', 123456789, 'patient');
/*!40000 ALTER TABLE `patient` ENABLE KEYS */;

-- Dumping structure for table test1.record
CREATE TABLE IF NOT EXISTS `record` (
    `id` int(255) NOT NULL,
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

-- Dumping data for table test1.record: ~0 rows (approximately)
/*!40000 ALTER TABLE `record` DISABLE KEYS */;
INSERT INTO `record` (`id`, `record_type`, `edit`, `view`, `owner`, `patient`, `date`) VALUES
    (1, 'de', 'doctor', 'doctor', 'doctor', 'patient', '2017-09-30');
/*!40000 ALTER TABLE `record` ENABLE KEYS */;

-- Dumping structure for table test1.record_correspondence
CREATE TABLE IF NOT EXISTS `record_correspondence` (
    `doctor` varchar(255) NOT NULL,
    `record_id` int(255) NOT NULL,
    `notes` varchar(50) NOT NULL,
    KEY `FK_record_correspondence_record` (`record_id`),
    KEY `FK_record_correspondence_doctor` (`doctor`),
    CONSTRAINT `FK_record_correspondence_doctor` FOREIGN KEY (`doctor`) REFERENCES `doctor` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `FK_record_correspondence_record` FOREIGN KEY (`record_id`) REFERENCES `record` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table test1.record_correspondence: ~0 rows (approximately)
/*!40000 ALTER TABLE `record_correspondence` DISABLE KEYS */;
INSERT INTO `record_correspondence` (`doctor`, `record_id`, `notes`) VALUES
    ('doctor', 1, 'note');
/*!40000 ALTER TABLE `record_correspondence` ENABLE KEYS */;

-- Dumping structure for table test1.record_diagnosis
CREATE TABLE IF NOT EXISTS `record_diagnosis` (
    `record_id` int(255) NOT NULL,
    `date` date NOT NULL,
    `doctor` varchar(255) NOT NULL,
    `diagnosis` varchar(255) NOT NULL,
    KEY `FK_record_diagnosis_record` (`record_id`),
    KEY `FK_record_diagnosis_user` (`doctor`),
    CONSTRAINT `FK_record_diagnosis_record` FOREIGN KEY (`record_id`) REFERENCES `record` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `FK_record_diagnosis_user` FOREIGN KEY (`doctor`) REFERENCES `doctor` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table test1.record_diagnosis: ~0 rows (approximately)
/*!40000 ALTER TABLE `record_diagnosis` DISABLE KEYS */;
INSERT INTO `record_diagnosis` (`record_id`, `date`, `doctor`, `diagnosis`) VALUES
    (1, '2017-09-30', 'doctor', 'd');
/*!40000 ALTER TABLE `record_diagnosis` ENABLE KEYS */;

-- Dumping structure for table test1.record_doctor_exam
CREATE TABLE IF NOT EXISTS `record_doctor_exam` (
    `record_id` int(255) NOT NULL,
    `doctor` varchar(255) NOT NULL,
    `date` date NOT NULL,
    `notes` varchar(255) NOT NULL,
    KEY `FK_record_doctor_exam_record` (`record_id`),
    KEY `FK_record_doctor_exam_user` (`doctor`),
    CONSTRAINT `FK_record_doctor_exam_record` FOREIGN KEY (`record_id`) REFERENCES `record` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `FK_record_doctor_exam_user` FOREIGN KEY (`doctor`) REFERENCES `doctor` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table test1.record_doctor_exam: ~0 rows (approximately)
/*!40000 ALTER TABLE `record_doctor_exam` DISABLE KEYS */;
INSERT INTO `record_doctor_exam` (`record_id`, `doctor`, `date`, `notes`) VALUES
    (1, 'doctor', '2017-09-30', 'notes');
/*!40000 ALTER TABLE `record_doctor_exam` ENABLE KEYS */;

-- Dumping structure for table test1.record_insurance
CREATE TABLE IF NOT EXISTS `record_insurance` (
    `record_id` int(255) NOT NULL,
    `date` date NOT NULL,
    `madmin` varchar(255) NOT NULL,
    `amount` float NOT NULL,
    `status` varchar(255) NOT NULL,
    KEY `FK_record_insurance_record` (`record_id`),
    KEY `FK_record_insurance_user` (`madmin`),
    CONSTRAINT `FK_record_insurance_record` FOREIGN KEY (`record_id`) REFERENCES `record` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `FK_record_insurance_user` FOREIGN KEY (`madmin`) REFERENCES `medical_admin` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table test1.record_insurance: ~0 rows (approximately)
/*!40000 ALTER TABLE `record_insurance` DISABLE KEYS */;
INSERT INTO `record_insurance` (`record_id`, `date`, `madmin`, `amount`, `status`) VALUES
    (1, '2017-09-30', 'medad', 123, 's');
/*!40000 ALTER TABLE `record_insurance` ENABLE KEYS */;

-- Dumping structure for table test1.record_note
CREATE TABLE IF NOT EXISTS `record_note` (
    `note_id` int(255) NOT NULL,
    `date` date NOT NULL,
    `text` varchar(255) NOT NULL,
    PRIMARY KEY (`note_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table test1.record_note: ~0 rows (approximately)
/*!40000 ALTER TABLE `record_note` DISABLE KEYS */;
INSERT INTO `record_note` (`note_id`, `date`, `text`) VALUES
    (1, '2017-09-30', '1');
/*!40000 ALTER TABLE `record_note` ENABLE KEYS */;

-- Dumping structure for table test1.record_raw
CREATE TABLE IF NOT EXISTS `record_raw` (
    `record_id` int(11) NOT NULL,
    `description` char(255) NOT NULL,
    `file` binary(255) NOT NULL,
    KEY `FK_record_raw_record` (`record_id`),
    CONSTRAINT `FK_record_raw_record` FOREIGN KEY (`record_id`) REFERENCES `record` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table test1.record_raw: ~0 rows (approximately)
/*!40000 ALTER TABLE `record_raw` DISABLE KEYS */;
INSERT INTO `record_raw` (`record_id`, `description`, `file`) VALUES
    (1, 'a', '3\04\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0');
/*!40000 ALTER TABLE `record_raw` ENABLE KEYS */;

-- Dumping structure for table test1.record_testresult
CREATE TABLE IF NOT EXISTS `record_testresult` (
    `record_id` int(11) NOT NULL,
    `date` date NOT NULL,
    `doctor` varchar(255) NOT NULL,
    `lab` varchar(255) NOT NULL,
    `notes` varchar(255) NOT NULL,
    KEY `FK_record_testresult_record` (`record_id`),
    KEY `FK_record_testresult_user` (`doctor`),
    CONSTRAINT `FK_record_testresult_record` FOREIGN KEY (`record_id`) REFERENCES `record` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `FK_record_testresult_user` FOREIGN KEY (`doctor`) REFERENCES `doctor` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table test1.record_testresult: ~0 rows (approximately)
/*!40000 ALTER TABLE `record_testresult` DISABLE KEYS */;
INSERT INTO `record_testresult` (`record_id`, `date`, `doctor`, `lab`, `notes`) VALUES
    (1, '2017-09-30', 'doctor', 'a', 'aids');
/*!40000 ALTER TABLE `record_testresult` ENABLE KEYS */;

-- Dumping structure for table test1.role
CREATE TABLE IF NOT EXISTS `role` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(255) NOT NULL,
    `permissions` varchar(255) NOT NULL,
    `role_info` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

-- Dumping data for table test1.role: ~6 rows (approximately)
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` (`id`, `name`, `permissions`, `role_info`) VALUES
    (1, 'ROLE_ADMIN', 'apat, epat. adoc, edoc, amed, emed, anurse, enurse, asys, esys, duser, aper, eraccess', 'System administraot'),
    (2, 'ROLE_DOCTOR', 'apat, epat, amed, emed, anurse, enurse', 'Doctor'),
    (3, 'ROLE_NURSE', 'apat, epat', 'Nurse'),
    (4, 'ROLE_MAD', 'apat, epat, pii', 'Medical Administration'),
    (5, 'ROLE_IAD', 'pii', 'Insurance Admin'),
    (6, 'ROLE_PATIENT', 'none', 'Patient');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;

-- Dumping structure for table test1.role_user
CREATE TABLE IF NOT EXISTS `role_user` (
    `id` int(11) NOT NULL,
    `uname` varchar(255) NOT NULL,
    KEY `FK__role` (`id`),
    KEY `FK__user` (`uname`),
    CONSTRAINT `FK__role` FOREIGN KEY (`id`) REFERENCES `role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `FK__user` FOREIGN KEY (`uname`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table test1.role_user: ~6 rows (approximately)
/*!40000 ALTER TABLE `role_user` DISABLE KEYS */;
INSERT INTO `role_user` (`id`, `uname`) VALUES
    (1, 'sysad'),
    (2, 'doctor'),
    (5, 'iad'),
    (4, 'medad'),
    (3, 'nurse'),
    (6, 'patient');
/*!40000 ALTER TABLE `role_user` ENABLE KEYS */;

-- Dumping structure for table test1.user
CREATE TABLE IF NOT EXISTS `user` (
    `password` varchar(255) NOT NULL,
    `username` varchar(255) NOT NULL,
    `Fname` char(255) NOT NULL,
    `Lname` char(255) NOT NULL,
    PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table test1.user: ~6 rows (approximately)
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`password`, `username`, `Fname`, `Lname`) VALUES
    ('doctor', 'doctor', 'doctor', 'doctor'),
    ('iad', 'iad', 'iad', 'iad'),
    ('medad', 'medad', 'medad', 'medad'),
    ('nurse', 'nurse', 'nurse', 'nurse'),
    ('patient', 'patient', 'patient', 'patient'),
    ('sysad', 'sysad', 'sysad', 'sysad');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;