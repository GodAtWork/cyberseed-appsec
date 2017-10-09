--------------------------------
--  Records of `user_permissions_list`
-- ----------------------------
BEGIN;
INSERT INTO `user_permissions_list` VALUES ('ROLE_DOCTOR', '{\"roles\":[\"ROLE_ADD_PATIENT\",\"ROLE_EDIT_PATIENT\",\"ROLE_ADD_MEDICAL_ADMIN\",\"ROLE_EDIT_MEDICAL_ADMIN\",\"ROLE_ADD_NURSE\",\"ROLE_EDIT_NURSE\"]}'), ('ROLE_INSURANCE_ADMIN', '{\"roles\":[\"ROLE_VIEW_PII\",\"ROLE_VIEW_PII\"]}'), ('ROLE_MEDICAL_ADMIN', '{\"roles\":[\"ROLE_ADD_PATIENT\",\"ROLE_EDIT_PATIENT\",\"ROLE_VIEW_PII\"]}'), ('ROLE_NURSE', '{\"roles\":[\"ROLE_ADD_PATIENT\",\"ROLE_EDIT_PATIENT\"]}'), ('ROLE_PATIENT', '{\"roles\":[\"ROLE_PATIENT\",\"ROLE_PATIENT\"]}'), ('ROLE_SYSTEM_ADMIN', '{\"roles\":[\"ROLE_ADD_PATIENT\",\"ROLE_EDIT_PATIENT\",\"ROLE_ADD_DOCTOR\",\"ROLE_EDIT_DOCTOR\",\"ROLE_ADD_MEDICAL_ADMIN\",\"ROLE_EDIT_MEDICAL_ADMIN\",\"ROLE_ADD_INSURANCE_ADMIN\",\"ROLE_EDIT_INSURANCE_ADMIN\",\"ROLE_ADD_NURSE\",\"ROLE_EDIT_NURSE\",\"ROLE_ADD_SYSTEM_ADMIN\",\"ROLE_EDIT_SYSTEM_ADMIN\",\"ROLE_DELETE_USER_PROFILE\",\"ROLE_ASSIGN_PERMISSIONS\",\"ROLE_EDIT_RECORD_ACCESS\"]}');
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
