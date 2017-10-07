
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Records of `doctor`
-- ----------------------------
BEGIN;
INSERT INTO `doctor` VALUES ('doctor', 'doctor', 'doctor', 'doctor');
COMMIT;

-- ----------------------------
--  Records of `insurance_admin`
-- ----------------------------
BEGIN;
INSERT INTO `insurance_admin` VALUES ('iad', 'iad', 'iad');
COMMIT;

-- ----------------------------
--  Records of `medical_admin`
-- ----------------------------
BEGIN;
INSERT INTO `medical_admin` VALUES ('medad', 'medad', 'medad', 'doctor', 'nurse');
COMMIT;

-- ----------------------------
--  Records of `nurse`
-- ----------------------------
BEGIN;
INSERT INTO `nurse` VALUES ('nurse', 'nurse', 'nurse', 'doctor');
COMMIT;

-- ----------------------------
--  Records of `patient`
-- ----------------------------
BEGIN;
INSERT INTO `patient` VALUES ('patient', '2017-09-30', '123456789', 'patient');
COMMIT;

-- ----------------------------
--  Records of `record`
-- ----------------------------
BEGIN;
INSERT INTO `record` VALUES ('1', 'de', 'doctor', 'doctor', 'doctor', 'patient', '2017-09-30');
COMMIT;

-- ----------------------------
--  Records of `record_correspondence`
-- ----------------------------
BEGIN;
INSERT INTO `record_correspondence` VALUES ('doctor', '1', 'note');
COMMIT;

-- -------------------------
--  Records of `record_diagnosis`
-- ----------------------------
BEGIN;
INSERT INTO `record_diagnosis` VALUES ('1', '2017-09-30', 'doctor', 'd');
COMMIT;

-- ----------------------------
--  Records of `record_doctor_exam`
-- ----------------------------
BEGIN;
INSERT INTO `record_doctor_exam` VALUES ('1', 'doctor', '2017-09-30', 'notes');
COMMIT;

-- ----------------------------
--  Records of `record_insurance`
-- ----------------------------
BEGIN;
INSERT INTO `record_insurance` VALUES ('1', '2017-09-30', 'medad', '123', 's');
COMMIT;

-- ----------------------------
--  Records of `record_note`
-- ----------------------------
BEGIN;
INSERT INTO `record_note` VALUES ('1', '2017-09-30', '1');
COMMIT;

-- ----------------------------
--  Records of `record_raw`
-- ----------------------------
-- BEGIN;
-- INSERT INTO `record_raw` VALUES ('1', 'a', 0x330034000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000);
-- COMMIT;

-- ----------------------------
--  Records of `record_testresult`
-- ----------------------------
BEGIN;
INSERT INTO `record_testresult` VALUES ('1', '2017-09-30', 'doctor', 'a', 'aids');
COMMIT;

-- ----------------------------
--  Records of `user`
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES ('$2a$11$dhneQwwEDBo2MJoXR5rurOuucOPdVlRwlmgZfj6ePId5DrviKjEp2', 'doctor', 'Jane', 'Doe', '{\"roles\":[\"ROLE_USER\",\"ROLE_DOCTOR\"]}', null, null), ('$2a$11$dhneQwwEDBo2MJoXR5rurOuucOPdVlRwlmgZfj6ePId5DrviKjEp2', 'iad', 'Jeb', 'Downs', '{\"roles\":[\"ROLE_USER\",\"ROLE_INSURANCE_ADMIN\"]}', null, null), ('$2a$11$dhneQwwEDBo2MJoXR5rurOuucOPdVlRwlmgZfj6ePId5DrviKjEp2', 'medad', 'Joe', 'Davids', '{\"roles\":[\"ROLE_USER\",\"ROLE_MEDICAL_ADMIN\"]}', null, null), ('$2a$11$dhneQwwEDBo2MJoXR5rurOuucOPdVlRwlmgZfj6ePId5DrviKjEp2', 'nurse', 'Jose', 'Davidson', '{\"roles\":[\"ROLE_USER\",\"ROLE_NURSE\"]}', null, null), ('$2a$11$dhneQwwEDBo2MJoXR5rurOuucOPdVlRwlmgZfj6ePId5DrviKjEp2', 'patient', 'Jaunita', 'Dun', '{\"roles\":[\"ROLE_USER\",\"ROLE_PATIENT\"]}', null, null), ('$2a$11$dhneQwwEDBo2MJoXR5rurOuucOPdVlRwlmgZfj6ePId5DrviKjEp2', 'patient11', 'Jerry', 'Smith', '{\"roles\":[\"ROLE_USER\",\"ROLE_PATIENT\"]}', null, null), ('$2a$11$dhneQwwEDBo2MJoXR5rurOuucOPdVlRwlmgZfj6ePId5DrviKjEp2', 'sysad', 'Jake', 'Macklin', '{\"roles\":[\"ROLE_USER\",\"ROLE_SYSTEM_ADMIN\"]}', null, null);
COMMIT;

-- ----------------------------
--  Records of `user_permissions_list`
-- ----------------------------
BEGIN;
INSERT INTO `user_permissions_list` VALUES ('ROLE_DOCTOR', '{\"roles\":[\"ROLE_ADD_PATIENT\",\"ROLE_EDIT_PATIENT\",\"ROLE_ADD_MEDICAL_ADMIN\",\"ROLE_EDIT_MEDICAL_ADMIN\",\"ROLE_ADD_NURSE\",\"ROLE_EDIT_NURSE\"]}'), ('ROLE_INSURANCE_ADMIN', '{\"roles\":[\"ROLE_VIEW_PII\",\"\"]}'), ('ROLE_MEDICAL_ADMIN', '{\"roles\":[\"ROLE_ADD_PATIENT\",\"ROLE_EDIT_PATIENT\",\"ROLE_VIEW_PII\"]}'), ('ROLE_NURSE', '{\"roles\":[\"ROLE_ADD_PATIENT\",\"ROLE_EDIT_PATIENT\"]}'), ('ROLE_PATIENT', '{\"roles\":[\"\"]}'), ('ROLE_SYSTEM_ADMIN', '{\"roles\":[\"ROLE_ADD_PATIENT\",\"ROLE_EDIT_PATIENT\",\"ROLE_ADD_DOCTOR\",\"ROLE_EDIT_DOCTOR\",\"ROLE_ADD_MEDICAL_ADMIN\",\"ROLE_EDIT_MEDICAL_ADMIN\",\"ROLE_ADD_INSURANCE_ADMIN\",\"ROLE_EDIT_INSURANCE_ADMIN\",\"ROLE_ADD_NURSE\",\"ROLE_EDIT_NURSE\",\"ROLE_ADD_SYSTEM_ADMIN\",\"ROLE_EDIT_SYSTEM_ADMIN\",\"ROLE_DELETE_USER_PROFILE\",\"ROLE_ASSIGN_PERMISSIONS\",\"ROLE_EDIT_RECORD_ACCESS\"]}');
COMMIT;

-- ----------------------------
--  Records of `user_role_list`
-- ----------------------------
BEGIN;
INSERT INTO `user_role_list` VALUES ('ROLE_DOCTOR', 'A medical doctor.'), ('ROLE_INSURANCE_ADMIN', 'Insurance company representative.'), ('ROLE_MEDICAL_ADMIN', 'Administers information for doctors and nurses. Primary tasks include interfacing with Insurance Administrators.'), ('ROLE_NURSE', 'A medical nurse.'), ('ROLE_PATIENT', 'Medical Patient.'), ('ROLE_SYSTEM_ADMIN', 'Highest default permissions.  Administers the SMIRK system.'), ('ROLE_USER', 'Any user of SMIRK system.');
COMMIT;

-- ----------------------------
--  Records of `permissions_list`
-- ----------------------------
BEGIN;
INSERT INTO `permissions_list` VALUES ('ROLE_ADD_DOCTOR', 'Ability to add a doctor user profile to the system.'), ('ROLE_ADD_INSURANCE_ADMIN', 'Ability to add a insurance administrator user profile to the system.'), ('ROLE_ADD_MEDICAL_ADMIN', 'Ability to add a medical administrator user profile to the system.'), ('ROLE_ADD_NURSE', 'Ability to add a nurse user profile to the system.'), ('ROLE_ADD_PATIENT', 'Ability to add a patient user profile to the system.'), ('ROLE_ADD_SYSTEM_ADMIN', 'Ability to add a system administrator user profile to the system.'), ('ROLE_ASSIGN_PERMISSIONS', 'Ability to assign permissions to user profiles.'), ('ROLE_DELETE_USER_PROFILE', 'Ability to delete an existing user profile.'), ('ROLE_EDIT_DOCTOR', 'Ability to edit an existing doctor user profile to the system.'), ('ROLE_EDIT_INSURANCE_ADMIN', 'Ability to edit an existing insurance administrator user profile information.'), ('ROLE_EDIT_MEDICAL_ADMIN', 'Ability to edit an existing medical administrator user profile information.'), ('ROLE_EDIT_NURSE', 'Ability to edit an existing nurse user profile information.'), ('ROLE_EDIT_PATIENT', 'Ability to edit an existing patient user profile information.'), ('ROLE_EDIT_RECORD_ACCESS', 'Ability to edit record View Permissions and Edit Permissions lists fields.'), ('ROLE_EDIT_SYSTEM_ADMIN', 'Abiltity to edit an existing system administrator user profile information.'), ('ROLE_USER', 'A user of the system.'), ('ROLE_VIEW_PII', 'Ability to view personally identifiable information (PII) held in the system.');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
