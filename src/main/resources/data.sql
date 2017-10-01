
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

----------------------------
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
BEGIN;
INSERT INTO `record_raw` VALUES ('1', 'a', 0x330034000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000);
COMMIT;

-- ----------------------------
--  Records of `record_testresult`
-- ----------------------------
BEGIN;
INSERT INTO `record_testresult` VALUES ('1', '2017-09-30', 'doctor', 'a', 'aids');
COMMIT;


-- ----------------------------
--  Records of `role`
-- ----------------------------
BEGIN;
INSERT INTO `role` VALUES ('1', 'ROLE_ADMIN', 'apat, epat. adoc, edoc, amed, emed, anurse, enurse, asys, esys, duser, aper, eraccess', 'System administraot'), ('2', 'ROLE_DOCTOR', 'apat, epat, amed, emed, anurse, enurse', 'Doctor'), ('3', 'ROLE_NURSE', 'apat, epat', 'Nurse'), ('4', 'ROLE_MAD', 'apat, epat, pii', 'Medical Administration'), ('5', 'ROLE_IAD', 'pii', 'Insurance Admin'), ('6', 'ROLE_PATIENT', 'none', 'Patient');
COMMIT;


-- ----------------------------
--  Records of `role_user`
-- ----------------------------
BEGIN;
INSERT INTO `role_user` VALUES ('1', 'sysad'), ('2', 'doctor'), ('5', 'iad'), ('4', 'medad'), ('3', 'nurse'), ('6', 'patient');
COMMIT;

-- ----------------------------
--  Records of `user`
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES ('doctor', 'doctor', 'doctor', 'doctor'), ('iad', 'iad', 'iad', 'iad'), ('medad', 'medad', 'medad', 'medad'), ('nurse', 'nurse', 'nurse', 'nurse'), ('patient', 'patient', 'patient', 'patient'), ('sysad', 'sysad', 'sysad', 'sysad');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
