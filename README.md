POST-COMPETITION UPDATE: No Vulnerabilities were exploited in this application during the competition.  Our team (Sage) placed second in the Application Security competition, and was recognized as the top offensive team in the Application Security competition.
### CyberSeed 2017
### Application Security Competition
### Secure Medical Information Repository Kit (SMIRK)

Server (https://github.com/kjfallon/cyberseed-appsec)  
Integration Test Client (https://github.com/kjfallon/cyberseed-appsec-integration-client)  
Backdoor Client (https://github.com/kjfallon/cyberseed-appsec-backdoor-client)

SMIRK is a web based system intended to allow users (Doctors, Nurses, Patients, Insurance Providers, and Medical Administrators) to securely share medical files. The system will be comprised of 3 major components: a server application, an integration test client application, and a backdoor client application. 

The server application will expose web accessible interfaces that comply to the interface requirements in this document. These interfaces will allow the client to administer the application state (user info, permissions, etc.), and store and retrieve medical information. 

### Database

- [x] Design Schema
- [x] Implement Schema

### Server Interface

- [x] Create API framework
- [x] /createPatient
- [x] /createDoctor	
- [x] /createNurse	
- [x] /createSysAdmin	
- [x] /createMedAdmin	
- [x] /createInsAdmin	
- [x] /editPerm	
- [x] /addDoctorExamRecord	
- [x] /addTestResultRecord	
- [x] /addDiagnosisRecord	
- [x] /addInsuranceClaimRecord	
- [x] /addRawRecord	
- [x] /createCorrespondenceRecord	
- [x] /addCorrespondenceNote	
- [x] /listRecords	
- [x] /viewRecord	
- [x] /editRecordPerm	
- [x] /editPatient	
- [x] /editDoctor	
- [x] /editNurse	
- [x] /editSysAdmin	
- [x] /editMedAdmin	
- [x] /editInsAdmin	
- [x] /viewPatientProfile
- [x] /viewRecoveryPhrase
- [x] /removeUserProfile
