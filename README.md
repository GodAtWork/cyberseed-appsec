### CyberSeed 2017 Application Security Competition
### Secure Medical Information Repository Kit (SMIRK) System Requirements Specification 

### Database

- [x] Design Schema
- [x] Implement Schema

### Server Interface

- [x] Create API framework
- [x] /createPatient
- [x] /createDoctor	
- [x] /createNurse	
- [x] /createSysAdmin	
- [ ] /createMedAdmin	
- [ ] /createInsAdmin	
- [x] /editPerm	
- [x] /addDoctorExamRecord	
- [ ] /addTestResultRecord	
- [x] /addDiagnosisRecord	
- [ ] /addInsuranceClaimRecord	
- [ ] /addRawRecord	
- [ ] /createCorrespondenceRecord	
- [ ] /addCorrespondenceNote	
- [x] /listRecords	
- [x] /viewRecord	
- [ ] /editRecordPerm	
- [x] /editPatient	
- [x] /editDoctor	
- [ ] /editNurse	
- [ ] /editSysAdmin	
- [ ] /editMedAdmin	
- [ ] /editInsAdmin	
- [ ] /viewPatientProfile
- [x] /viewRecoveryPhrase
- [ ] /removeUserProfile

### Integration Test Client

- [ ] Author standalone API client 
- [ ] Add test sequence to client
- [ ] Verify expected results

### Backdoor Client

- [ ] Author standalone direct DB client
- [ ] setITAdmin
- [ ] loadData
- [ ] loadBackupCfg
- [ ] getBackupCfg
- [ ] DumpDB