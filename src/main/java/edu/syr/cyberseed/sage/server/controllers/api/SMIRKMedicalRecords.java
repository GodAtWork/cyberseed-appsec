package edu.syr.cyberseed.sage.server.controllers.api;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.prism.impl.Disposer;
import edu.syr.cyberseed.sage.server.entities.*;
import edu.syr.cyberseed.sage.server.entities.models.*;
import edu.syr.cyberseed.sage.server.repositories.*;
import flexjson.JSONSerializer;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@RestController
public class SMIRKMedicalRecords {

    @Autowired
    MedicalRecordRepository medicalRecordRepository;
    @Autowired
    MedicalRecordWithoutAutoIdRepository medicalRecordWithoutAutoIdRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    MedicalAdminRepository medicalAdminRepository;
    @Autowired
    InsuranceAdminRepository insuranceAdminRepository;
    @Autowired
    PermissionsRepository permissionListRepository;
    @Autowired
    DoctorExamRecordRepository doctorExamRecordRepository;
    @Autowired
    TestResultRecordRepository testResultRecordRepository;
    @Autowired
    DiagnosisRecordRepository diagnosisRecordRepository;
    @Autowired
    InsuranceClaimRecordRepository insuranceClaimRecordRepository;
    @Autowired
    CorrespondenceRecordRepository correspondenceRecordRepository;
    @Autowired
    RawRecordRepository rawRecordRepository;

    private static final Logger logger = LoggerFactory.getLogger(SMIRKMedicalRecords.class);

    // 5.8 /addDoctorExamRecord
    @Secured({"ROLE_DOCTOR","ROLE_NURSE","ROLE_MEDICAL_ADMIN"})
    @ApiOperation(value = "Add a Doctor Exam MedicalRecord to the database.",
            notes = "When addDoctorExam MedicalRecord is successfully exercised, the result SHALL be a new Doctor Exam MedicalRecord with valid non-null values added to the database.  The addDoctorExamRecord service SHALL only be accessible to users with the Doctor, Nurse, and Medical Administrator roles.")
    @RequestMapping(value = "/addDoctorExamRecord", method = RequestMethod.POST)
    public ResultValue addDoctorExamRecord(@RequestBody @Valid DoctorExamRecordModel submittedData) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Authenticated user " + currentUser + " is starting execution of service /addDoctorExamRecord");
        String resultString = "FAILURE";

        Doctor possibleExistingDoctor = doctorRepository.findByUsername(submittedData.getDoctorUsername());
        Boolean doctorExists = (possibleExistingDoctor != null) ? true : false;
        Patient possibleExistingPatient = patientRepository.findByUsername(submittedData.getPatientUsername());
        Boolean patientExists = (possibleExistingPatient != null) ? true : false;

        if (doctorExists && patientExists) {

            //Check if API user specified supplemental users for edit or view permission
            Boolean editUsersSubmitted = ((submittedData.getEdit() != null) && (submittedData.getEdit().size() > 0)) ? true : false;
            Boolean viewUsersSubmitted = ((submittedData.getView() != null) && (submittedData.getView().size() > 0)) ? true : false;

            // create a json object of the default edit users
            ArrayList<String> editUserList = new ArrayList<String>();
            // by default do not add any users
            //editUserList.add(currentUser);
            Map<String, Object> editUserListJson = new HashMap<String, Object>();


            // create a json object of the default view users
            ArrayList<String> viewUserList = new ArrayList<String>();
            // by default do not add any users
            //viewUserList.add(currentUser);
            //viewUserList.add(submittedData.getPatientUsername());
            Map<String, Object> viewUserListJson = new HashMap<String, Object>();


            String finalEditPermissions = "";
            String finalViewPermissions = "";

            if (editUsersSubmitted) {
                List<String> userSuppliedListOfUsersToGrantEdit = submittedData.getEdit();
                for (String username : userSuppliedListOfUsersToGrantEdit) {
                    User possibleUser = userRepository.findByUsername(username);
                    if ((possibleUser != null) && (StringUtils.isNotEmpty(possibleUser.getUsername()))) {
                        editUserList.add(username);
                    }
                }
                editUserListJson.put("users", editUserList);
                JSONSerializer serializer = new JSONSerializer();
                finalEditPermissions = serializer.include("users").serialize(editUserListJson);
            }
            else {
                editUserListJson.put("users", editUserList);
                JSONSerializer serializer = new JSONSerializer();
                finalEditPermissions = serializer.include("users").serialize(editUserListJson);
            }

            if (viewUsersSubmitted) {
                List<String> userSuppliedListOfUsersToGrantView = submittedData.getView();
                for (String username : userSuppliedListOfUsersToGrantView) {
                    User possibleUser = userRepository.findByUsername(username);
                    if ((possibleUser != null) && (StringUtils.isNotEmpty(possibleUser.getUsername()))) {
                        viewUserList.add(username);
                    }
                }
                viewUserListJson.put("users", viewUserList);
                JSONSerializer serializer = new JSONSerializer();
                finalViewPermissions = serializer.include("users").serialize(viewUserListJson);
            }
            else {
                viewUserListJson.put("users", viewUserList);
                JSONSerializer serializer = new JSONSerializer();
                finalViewPermissions = serializer.include("users").serialize(viewUserListJson);
            }

            // was a record id specified?
            logger.info("Submitted record id is " + submittedData.getId());
            if (submittedData.getId() != null) {
                MedicalRecord possibleExistingRecord = medicalRecordRepository.findById(submittedData.getId());
                DoctorExamRecord possibleExistingDoctorExamRecord = doctorExamRecordRepository.findById(submittedData.getId());
                Boolean recordExists = (possibleExistingRecord != null) ? true : false;
                Boolean doctorExamRecordExists = (possibleExistingDoctorExamRecord != null) ? true : false;

                if (recordExists || doctorExamRecordExists) {
                    logger.error("Cannot create doctor exam record due to recordExists=" + recordExists + " and doctorExamRecordExists=" + doctorExamRecordExists
                            + ". You cannot create *new* records with a specific id if records already exist with that id.");
                }
                else {
                    logger.info("Creating records with id " + submittedData.getId());
                    MedicalRecordWithoutAutoId savedMedicalRecord = medicalRecordWithoutAutoIdRepository.save(new MedicalRecordWithoutAutoId(submittedData.getId(),
                            "Doctor Exam",
                            new Date(),
                            currentUser,
                            submittedData.getPatientUsername(),
                            finalEditPermissions,
                            finalViewPermissions));
                    logger.info("Created  MedicalRecord with id " + savedMedicalRecord.getId());

                    // create the Doctor exam record
                    DoctorExamRecord savedDoctorExamRecord = doctorExamRecordRepository.save(new DoctorExamRecord(submittedData.getId(),
                            submittedData.getDoctorUsername(),
                            submittedData.getExamDate(),
                            submittedData.getNotes()));
                    logger.info("Created  DoctorExamRecord with id " + savedDoctorExamRecord.getId());
                    resultString = "SUCCESS";
                }
            }
            else {
                try {
                    // create the record
                    MedicalRecord savedMedicalRecord = medicalRecordRepository.save(new MedicalRecord("Doctor Exam",
                            new Date(),
                            currentUser,
                            submittedData.getPatientUsername(),
                            finalEditPermissions,
                            finalViewPermissions));
                    logger.info("Created  MedicalRecord with id " + savedMedicalRecord.getId());

                    // create the Doctor exam record
                    // Use id auto assigned by db to MedicalRecord for examRecord
                    DoctorExamRecord savedDoctorExamRecord = doctorExamRecordRepository.save(new DoctorExamRecord(savedMedicalRecord.getId(),
                            submittedData.getDoctorUsername(),
                            submittedData.getExamDate(),
                            submittedData.getNotes()));
                    logger.info("Created  DoctorExamRecord with id " + savedDoctorExamRecord.getId());


                    resultString = "SUCCESS";
                    logger.info("Created doctor exam record for doctor " + submittedData.getDoctorUsername());
                } catch (Exception e) {
                    logger.error("Failure creating doctor exam record for doctor " + submittedData.getDoctorUsername());
                    e.printStackTrace();
                }
            }
        }
        else {
            logger.error("Cannot create doctor exam record due to doctorExists=" + doctorExists + " and patientExists=" +patientExists + ". Both need to exist.");
        }
        ResultValue result = new ResultValue();
        result.setResult(resultString);
        logger.info("Authenticated user " + currentUser + " completed execution of service /addDoctorExamRecord");
        return result;
    }

    //starting test result record
    // 5.9 /addTestResultRecord
    @Secured({"ROLE_DOCTOR","ROLE_NURSE","ROLE_MEDICAL_ADMIN"})
    @ApiOperation(value = "Add a Test Result MedicalRecord to the database.",
            notes = "When addTestResult MedicalRecord is successfully exercised, the result SHALL be a new Test Result MedicalRecord with valid non-null values added to the database.  The addTestResultRecord service SHALL only be accessible to users with the Doctor, Nurse, and Medical Administrator roles.")
    @RequestMapping(value = "/addTestResultRecord", method = RequestMethod.POST)
    public ResultValue addTestResultRecord(@RequestBody @Valid TestResultRecordModel submittedData) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Authenticated user " + currentUser + " is starting execution of service /addTestResultRecord");
        String resultString = "FAILURE";

        Doctor possibleExistingDoctor = doctorRepository.findByUsername(submittedData.getDoctorUsername());
        Boolean doctorExists = (possibleExistingDoctor != null) ? true : false;
        Patient possibleExistingPatient = patientRepository.findByUsername(submittedData.getPatientUsername());
        Boolean patientExists = (possibleExistingPatient != null) ? true : false;

        if (doctorExists && patientExists) {

            //Check if API user specified supplemental users for edit or view permission
            Boolean editUsersSubmitted = ((submittedData.getEdit() != null) && (submittedData.getEdit().size() > 0)) ? true : false;
            Boolean viewUsersSubmitted = ((submittedData.getView() != null) && (submittedData.getView().size() > 0)) ? true : false;

            // create a json object of the default edit users
            ArrayList<String> editUserList = new ArrayList<String>();
            // by default do not add any users
            //editUserList.add(currentUser);
            Map<String, Object> editUserListJson = new HashMap<String, Object>();


            // create a json object of the default view users
            ArrayList<String> viewUserList = new ArrayList<String>();
            // by default do not add any users
            //viewUserList.add(currentUser);
            //viewUserList.add(submittedData.getPatientUsername());
            Map<String, Object> viewUserListJson = new HashMap<String, Object>();


            String finalEditPermissions = "";
            String finalViewPermissions = "";

            if (editUsersSubmitted) {
                List<String> userSuppliedListOfUsersToGrantEdit = submittedData.getEdit();
                for (String username : userSuppliedListOfUsersToGrantEdit) {
                    User possibleUser = userRepository.findByUsername(username);
                    if ((possibleUser != null) && (StringUtils.isNotEmpty(possibleUser.getUsername()))) {
                        editUserList.add(username);
                    }
                }
                editUserListJson.put("users", editUserList);
                JSONSerializer serializer = new JSONSerializer();
                finalEditPermissions = serializer.include("users").serialize(editUserListJson);
            }
            else {
                editUserListJson.put("users", editUserList);
                JSONSerializer serializer = new JSONSerializer();
                finalEditPermissions = serializer.include("users").serialize(editUserListJson);
            }

            if (viewUsersSubmitted) {
                List<String> userSuppliedListOfUsersToGrantView = submittedData.getView();
                for (String username : userSuppliedListOfUsersToGrantView) {
                    User possibleUser = userRepository.findByUsername(username);
                    if ((possibleUser != null) && (StringUtils.isNotEmpty(possibleUser.getUsername()))) {
                        viewUserList.add(username);
                    }
                }
                viewUserListJson.put("users", viewUserList);
                JSONSerializer serializer = new JSONSerializer();
                finalViewPermissions = serializer.include("users").serialize(viewUserListJson);
            }
            else {
                viewUserListJson.put("users", viewUserList);
                JSONSerializer serializer = new JSONSerializer();
                finalViewPermissions = serializer.include("users").serialize(viewUserListJson);
            }

            // was a record id specified?
            logger.info("Submitted record id is " + submittedData.getId());
            if (submittedData.getId() != null) {
                MedicalRecord possibleExistingRecord = medicalRecordRepository.findById(submittedData.getId());
                TestResultRecord possibleExistingTestResultRecord = testResultRecordRepository.findById(submittedData.getId());
                Boolean recordExists = (possibleExistingRecord != null) ? true : false;
                Boolean testResultRecordExists = (possibleExistingTestResultRecord != null) ? true : false;

                if (recordExists || testResultRecordExists) {
                    logger.error("Cannot create test result record due to recordExists=" + recordExists + " and testResultRecordExists=" + testResultRecordExists
                            + ". You cannot create *new* records with a specific id if records already exist with that id.");
                }
                else {
                    logger.info("Creating records with id " + submittedData.getId());
                    MedicalRecordWithoutAutoId savedMedicalRecord = medicalRecordWithoutAutoIdRepository.save(new MedicalRecordWithoutAutoId(submittedData.getId(),
                            "Test Result",
                            new Date(),
                            currentUser,
                            submittedData.getPatientUsername(),
                            finalEditPermissions,
                            finalViewPermissions));
                    logger.info("Created  MedicalRecord with id " + savedMedicalRecord.getId());

                    // create the Test Result record
                    TestResultRecord savedTestResultRecord = testResultRecordRepository.save(new TestResultRecord(submittedData.getId(),
                            submittedData.getDoctorUsername(),
                            submittedData.getLab(),
                            submittedData.getNotes(),submittedData.getTestDate()));
                    logger.info("Created  TestResultRecord with id " + savedTestResultRecord.getId());
                    resultString = "SUCCESS";
                }
            }
            else {
                try {
                    // create the record
                    MedicalRecord savedMedicalRecord = medicalRecordRepository.save(new MedicalRecord("Test Result",
                            new Date(),
                            currentUser,
                            submittedData.getPatientUsername(),
                            finalEditPermissions,
                            finalViewPermissions));
                    logger.info("Created  MedicalRecord with id " + savedMedicalRecord.getId());

                    // create the Test Result record
                    // Use id auto assigned by db to MedicalRecord
                    TestResultRecord savedTestResultRecord = testResultRecordRepository.save(new TestResultRecord(submittedData.getId(),
                            submittedData.getDoctorUsername(),
                            submittedData.getLab(),
                            submittedData.getNotes(),submittedData.getTestDate()));
                    logger.info("Created  TestResultRecord with id " + savedTestResultRecord.getId());


                    resultString = "SUCCESS";
                    logger.info("Created Test Result record for doctor " + submittedData.getDoctorUsername());
                } catch (Exception e) {
                    logger.error("Failure creating test result record for doctor " + submittedData.getDoctorUsername());
                    e.printStackTrace();
                }
            }
        }
        else {
            logger.error("Cannot create test result record due to doctorExists=" + doctorExists + " and patientExists=" +patientExists + ". Both need to exist.");
        }
        ResultValue result = new ResultValue();
        result.setResult(resultString);
        logger.info("Authenticated user " + currentUser + " completed execution of service /addTestResultRecord");
        return result;
    }

    //ending test result record

    // 5.10 /addDiagnosisRecord
    @Secured({"ROLE_DOCTOR"})
    @ApiOperation(value = "Add a Diagnosis Record to the database.",
            notes = "When addDiagnosisRecord is successfully exercised, the result SHALL be a new Diagnosis Record with valid non-null values added to the database. The addDiagnosisRecord service SHALL only be accessible to users with the Doctor role.")
    @RequestMapping(value = "/addDiagnosisRecord", method = RequestMethod.POST)
    public ResultValue addDiagnosisRecord(@RequestBody @Valid DiagnosisRecordModel submittedData) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Authenticated user " + currentUser + " is starting execution of service /addDiagnosisRecord");
        String resultString = "FAILURE";

        Doctor possibleExistingDoctor = doctorRepository.findByUsername(submittedData.getDoctorUsername());
        Boolean doctorExists = (possibleExistingDoctor != null) ? true : false;
        Patient possibleExistingPatient = patientRepository.findByUsername(submittedData.getPatientUsername());
        Boolean patientExists = (possibleExistingPatient != null) ? true : false;

        if (doctorExists && patientExists) {
            //Check if API user specified supplemental users for edit or view permission
            Boolean editUsersSubmitted = ((submittedData.getEdit() != null) && (submittedData.getEdit().size() > 0)) ? true : false;
            Boolean viewUsersSubmitted = ((submittedData.getView() != null) && (submittedData.getView().size() > 0)) ? true : false;

            // create a json object of the default edit users
            ArrayList<String> editUserList = new ArrayList<String>();
            // by default do not add any users
            //editUserList.add(currentUser);
            Map<String, Object> editUserListJson = new HashMap<String, Object>();


            // create a json object of the default view users
            ArrayList<String> viewUserList = new ArrayList<String>();
            // by default do not add any users
            //viewUserList.add(currentUser);
            //viewUserList.add(submittedData.getPatientUsername());
            Map<String, Object> viewUserListJson = new HashMap<String, Object>();


            String finalEditPermissions = "";
            String finalViewPermissions = "";

            if (editUsersSubmitted) {
                List<String> userSuppliedListOfUsersToGrantEdit = submittedData.getEdit();
                for (String username : userSuppliedListOfUsersToGrantEdit) {
                    User possibleUser = userRepository.findByUsername(username);
                    if ((possibleUser != null) && (StringUtils.isNotEmpty(possibleUser.getUsername()))) {
                        editUserList.add(username);
                    }
                }
                editUserListJson.put("users", editUserList);
                JSONSerializer serializer = new JSONSerializer();
                finalEditPermissions = serializer.include("users").serialize(editUserListJson);
            }
            else {
                editUserListJson.put("users", editUserList);
                JSONSerializer serializer = new JSONSerializer();
                finalEditPermissions = serializer.include("users").serialize(editUserListJson);
            }

            if (viewUsersSubmitted) {
                List<String> userSuppliedListOfUsersToGrantView = submittedData.getView();
                for (String username : userSuppliedListOfUsersToGrantView) {
                    User possibleUser = userRepository.findByUsername(username);
                    if ((possibleUser != null) && (StringUtils.isNotEmpty(possibleUser.getUsername()))) {
                        viewUserList.add(username);
                    }
                }
                viewUserListJson.put("users", viewUserList);
                JSONSerializer serializer = new JSONSerializer();
                finalViewPermissions = serializer.include("users").serialize(viewUserListJson);
            }
            else {
                viewUserListJson.put("users", viewUserList);
                JSONSerializer serializer = new JSONSerializer();
                finalViewPermissions = serializer.include("users").serialize(viewUserListJson);
            }

            // was a record id specified?
            logger.info("Submitted record id is " + submittedData.getId());
            if (submittedData.getId() != null) {
                MedicalRecord possibleExistingRecord = medicalRecordRepository.findById(submittedData.getId());
                DiagnosisRecord possibleExistingDiagnosisRecord = diagnosisRecordRepository.findById(submittedData.getId());
                Boolean recordExists = (possibleExistingRecord != null) ? true : false;
                Boolean diagnosisRecordExists = (possibleExistingDiagnosisRecord != null) ? true : false;

                if (recordExists || diagnosisRecordExists) {
                    logger.error("Cannot create diagnosis record due to recordExists=" + recordExists + " and diagnosisRecordExists=" + diagnosisRecordExists
                            + ". You cannot create *new* records with a specific id if records already exist with that id.");
                }
                else {
                    logger.info("Creating records with id " + submittedData.getId());
                    MedicalRecordWithoutAutoId savedMedicalRecord = medicalRecordWithoutAutoIdRepository.save(new MedicalRecordWithoutAutoId(submittedData.getId(),
                            "Diagnosis Record",
                            new Date(),
                            currentUser,
                            submittedData.getPatientUsername(),
                            finalEditPermissions,
                            finalViewPermissions));
                    logger.info("Created  MedicalRecord with id " + savedMedicalRecord.getId());

                    // create the Diagnosis record
                    DiagnosisRecord savedDiagnosisRecord = diagnosisRecordRepository.save(new DiagnosisRecord(submittedData.getId(),
                            submittedData.getDoctorUsername(),
                            submittedData.getDiagnosisDate(),
                            submittedData.getDiagnosis()));
                    logger.info("Created  DiagnosisRecord with id " + savedDiagnosisRecord.getId());
                    resultString = "SUCCESS";
                }

            }
            else {
                try {
                    // create the record
                    MedicalRecord savedMedicalRecord = medicalRecordRepository.save(new MedicalRecord("Doctor Exam",
                            new Date(),
                            currentUser,
                            submittedData.getPatientUsername(),
                            finalEditPermissions,
                            finalViewPermissions));
                    logger.info("Created  MedicalRecord with id " + savedMedicalRecord.getId());

                    // create the Diagnosis record
                    // Use id auto assigned by db to MedicalRecord for examRecord
                    DiagnosisRecord savedDiagnosisRecord = diagnosisRecordRepository.save(new DiagnosisRecord(savedMedicalRecord.getId(),
                            submittedData.getDoctorUsername(),
                            submittedData.getDiagnosisDate(),
                            submittedData.getDiagnosis()));
                    logger.info("Created  DiagnosisRecord with id " + savedDiagnosisRecord.getId());


                    resultString = "SUCCESS";
                    logger.info("Created diagnosis record for doctor " + submittedData.getDoctorUsername());
                } catch (Exception e) {
                    logger.error("Failure creating diagnosis record for doctor " + submittedData.getDoctorUsername());
                    e.printStackTrace();
                }
            }
        }
        else {
            logger.error("Cannot create diagnosis record due to doctorExists=" + doctorExists + " and patientExists=" +patientExists + ". Both need to exist.");
        }
        ResultValue result = new ResultValue();
        result.setResult(resultString);
        logger.info("Authenticated user " + currentUser + " completed execution of service /addDiagnosisRecord");
        return result;
    }

    //starting insurance claim record
    // 5.11 /addInsuranceClaimRecord

    @Secured({"ROLE_MEDICAL_ADMIN","ROLE_INSURANCE_ADMIN"})
    @ApiOperation(value = "Add a Insurance Record to the database.",
            notes = "When addRecordInsurance is successfully exercised, the result SHALL be a new Insurance Record with valid non-null values added to the database. The addRecordInsurance service SHALL only be accessible to users with the Insurance Admin role.")
    @RequestMapping(value = "/addInsuranceClaimRecord", method = RequestMethod.POST)
    public ResultValue addInsuranceClaimRecord(@RequestBody @Valid InsuranceClaimRecordModel submittedData) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Authenticated user " + currentUser + " is starting execution of service /addInsuranceClaimRecord");
        String resultString = "FAILURE";

        Medical_admin possibleExistingMedicalAdmin = medicalAdminRepository.findByUsername(submittedData.getMedadUsername());
        Boolean MedAdminExists = (possibleExistingMedicalAdmin != null) ? true : false;
        Patient possibleExistingPatient = patientRepository.findByUsername(submittedData.getPatientUsername());
        Boolean patientExists = (possibleExistingPatient != null) ? true : false;

        if (MedAdminExists && patientExists) {

            //Check if API user specified supplemental users for edit or view permission
            Boolean editUsersSubmitted = ((submittedData.getEdit() != null) && (submittedData.getEdit().size() > 0)) ? true : false;
            Boolean viewUsersSubmitted = ((submittedData.getView() != null) && (submittedData.getView().size() > 0)) ? true : false;

            // create a json object of the default edit users
            ArrayList<String> editUserList = new ArrayList<String>();
            editUserList.add(currentUser);
            Map<String, Object> editUserListJson = new HashMap<String, Object>();


            // create a json object of the default view users
            ArrayList<String> viewUserList = new ArrayList<String>();
            viewUserList.add(currentUser);
            viewUserList.add(submittedData.getPatientUsername());
            Map<String, Object> viewUserListJson = new HashMap<String, Object>();


            String finalEditPermissions = "";
            String finalViewPermissions = "";

            if (editUsersSubmitted) {
                List<String> userSuppliedListOfUsersToGrantEdit = submittedData.getEdit();
                for (String username : userSuppliedListOfUsersToGrantEdit) {
                    User possibleUser = userRepository.findByUsername(username);
                    if ((possibleUser != null) && (StringUtils.isNotEmpty(possibleUser.getUsername()))) {
                        editUserList.add(username);
                    }
                }
                editUserListJson.put("users", editUserList);
                JSONSerializer serializer = new JSONSerializer();
                finalEditPermissions = serializer.include("users").serialize(editUserListJson);
            }
            else {
                editUserListJson.put("users", editUserList);
                JSONSerializer serializer = new JSONSerializer();
                finalEditPermissions = serializer.include("users").serialize(editUserListJson);
            }

            if (viewUsersSubmitted) {
                List<String> userSuppliedListOfUsersToGrantView = submittedData.getView();
                for (String username : userSuppliedListOfUsersToGrantView) {
                    User possibleUser = userRepository.findByUsername(username);
                    if ((possibleUser != null) && (StringUtils.isNotEmpty(possibleUser.getUsername()))) {
                        viewUserList.add(username);
                    }
                }
                viewUserListJson.put("users", viewUserList);
                JSONSerializer serializer = new JSONSerializer();
                finalViewPermissions = serializer.include("users").serialize(viewUserListJson);
            }
            else {
                viewUserListJson.put("users", viewUserList);
                JSONSerializer serializer = new JSONSerializer();
                finalViewPermissions = serializer.include("users").serialize(viewUserListJson);
            }

            // was a record id specified?
            logger.info("Submitted record id is " + submittedData.getId());
            if (submittedData.getId() != null) {
                MedicalRecord possibleExistingRecord = medicalRecordRepository.findById(submittedData.getId());
                InsuranceClaimRecord possibleExistingInsuranceClaimRecord = insuranceClaimRecordRepository.findById(submittedData.getId());
                Boolean recordExists = (possibleExistingRecord != null) ? true : false;
                Boolean insuranceClaimRecordExists = (possibleExistingInsuranceClaimRecord != null) ? true : false;

                if (recordExists || insuranceClaimRecordExists) {
                    logger.error("Cannot create diagnosis record due to recordExists=" + recordExists + " and insuranceClaimRecordExists=" + insuranceClaimRecordExists
                            + ". You cannot create *new* records with a specific id if records already exist with that id.");
                }
                else {
                    if(submittedData.getStatus().equals("Filed") || submittedData.getStatus().equals("Rejected") || submittedData.getStatus().equals("Examining") || submittedData.getStatus().equals("Paid") || submittedData.getStatus().equals("Accepted")) {
                        logger.info("Creating records with id " + submittedData.getId());
                        MedicalRecordWithoutAutoId savedMedicalRecord = medicalRecordWithoutAutoIdRepository.save(new MedicalRecordWithoutAutoId(submittedData.getId(),
                                "Insurance Claim",
                                new Date(),
                                currentUser,
                                submittedData.getPatientUsername(),
                                finalEditPermissions,
                                finalViewPermissions));
                        logger.info("Created  MedicalRecord with id " + savedMedicalRecord.getId());

                        // create the Diagnosis record\
                        InsuranceClaimRecord savedInsuranceClaimRecord = insuranceClaimRecordRepository.save(new InsuranceClaimRecord(submittedData.getId(),
                                submittedData.getMedadUsername(),
                                submittedData.getDate(),
                                submittedData.getStatus(),
                                submittedData.getAmount()));
                        logger.info("Created  Insurance with id " + savedInsuranceClaimRecord.getId());

                        resultString = "SUCCESS";
                    }
                }

            }
            else {
                try {
                    if(submittedData.getStatus().equals("Filed") || submittedData.getStatus().equals("Rejected") || submittedData.getStatus().equals("Examining") || submittedData.getStatus().equals("Paid") || submittedData.getStatus().equals("Accepted")) {
                        // create the record
                        MedicalRecord savedMedicalRecord = medicalRecordRepository.save(new MedicalRecord("Insurance Claim",
                                new Date(),
                                currentUser,
                                submittedData.getPatientUsername(),
                                finalEditPermissions,
                                finalViewPermissions));
                        logger.info("Created  MedicalRecord with id " + savedMedicalRecord.getId());

                        // create the Diagnosis record
                        // Use id auto assigned by db to MedicalRecord for examRecord

                        resultString = "SUCCESS";
                        InsuranceClaimRecord savedInsuranceClaimRecord = insuranceClaimRecordRepository.save(new InsuranceClaimRecord(savedMedicalRecord.getId(),
                                submittedData.getMedadUsername(),
                                submittedData.getDate(),
                                submittedData.getStatus(),
                                submittedData.getAmount()));
                        logger.info("Created  DiagnosisRecord with id " + savedInsuranceClaimRecord.getId());
                    }
                } catch (Exception e) {
                    logger.error("Failure creating Insurance record :" + e);
                    e.printStackTrace();
                }
            }
        }
        else {
            logger.error("Cannot create diagnosis record due to doctorExists=" + MedAdminExists + " and patientExists=" +patientExists + ". Both need to exist.");
        }
        ResultValue result = new ResultValue();
        result.setResult(resultString);
        logger.info("Authenticated user " + currentUser + " completed execution of service /addInsuranceClaimRecord");
        return result;
    }
    //ending insurance claim record

    // 5.12 /addRawRecord
    @Secured({"ROLE_USER"})
    @ApiOperation(value = "Add a Raw Claim to the database.",
            notes = "When addRawRecord is successfully exercised, the result SHALL be a new Raw Record with valid non-null values added to the database. The addRawRecord service SHALL be accessible to all users. \n")
    @RequestMapping(value = "/addRawRecord", method = RequestMethod.POST)
    public ResultValue addRawRecord(@RequestBody @Valid RawRecordModel submittedData) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Authenticated user " + currentUser + " is starting execution of service /addRawRecord");
        String resultString = "FAILURE";

        byte[] fileAsBytes = null;
        // Was the user supplied file actually base64 encoded data?
        try {
            fileAsBytes = Base64.getDecoder().decode(submittedData.getBase64EncodedBinary());
        }
        catch (Exception e) {
            logger.warn("Authenticated user " + currentUser + " submitted an invalid base64 encoded file, Raw Record was not created");
            e.printStackTrace();
            ResultValue result = new ResultValue();
            result.setResult(resultString);
            logger.info("Authenticated user " + currentUser + " completed execution of service /addRawRecord");
            return result;
        }
        Integer fileByteLength = fileAsBytes.length;
        if (fileByteLength > 255) {
            logger.warn("Authenticated user " + currentUser + " user submitted more than 255 base64 encoded bytes, Raw Record was not created");
            ResultValue result = new ResultValue();
            result.setResult(resultString);
            logger.info("Authenticated user " + currentUser + " completed execution of service /addRawRecord");
            return result;
        }

        //Check if API user specified supplemental users for edit or view permission
        Boolean editUsersSubmitted = ((submittedData.getEdit() != null) && (submittedData.getEdit().size() > 0)) ? true : false;
        Boolean viewUsersSubmitted = ((submittedData.getView() != null) && (submittedData.getView().size() > 0)) ? true : false;

        // create a json object of the default edit users
        ArrayList<String> editUserList = new ArrayList<String>();
        // by default do not add any users
        //editUserList.add(currentUser);
        Map<String, Object> editUserListJson = new HashMap<String, Object>();


        // create a json object of the default view users
        ArrayList<String> viewUserList = new ArrayList<String>();
        // by default do not add any users
        //viewUserList.add(currentUser);
        //viewUserList.add(submittedData.getPatientUsername());
        Map<String, Object> viewUserListJson = new HashMap<String, Object>();


        String finalEditPermissions = "";
        String finalViewPermissions = "";

        if (editUsersSubmitted) {
            List<String> userSuppliedListOfUsersToGrantEdit = submittedData.getEdit();
            for (String username : userSuppliedListOfUsersToGrantEdit) {
                User possibleUser = userRepository.findByUsername(username);
                if ((possibleUser != null) && (StringUtils.isNotEmpty(possibleUser.getUsername()))) {
                    editUserList.add(username);
                }
            }
            editUserListJson.put("users", editUserList);
            JSONSerializer serializer = new JSONSerializer();
            finalEditPermissions = serializer.include("users").serialize(editUserListJson);
        }
        else {
            editUserListJson.put("users", editUserList);
            JSONSerializer serializer = new JSONSerializer();
            finalEditPermissions = serializer.include("users").serialize(editUserListJson);
        }

        if (viewUsersSubmitted) {
            List<String> userSuppliedListOfUsersToGrantView = submittedData.getView();
            for (String username : userSuppliedListOfUsersToGrantView) {
                User possibleUser = userRepository.findByUsername(username);
                if ((possibleUser != null) && (StringUtils.isNotEmpty(possibleUser.getUsername()))) {
                    viewUserList.add(username);
                }
            }
            viewUserListJson.put("users", viewUserList);
            JSONSerializer serializer = new JSONSerializer();
            finalViewPermissions = serializer.include("users").serialize(viewUserListJson);
        }
        else {
            viewUserListJson.put("users", viewUserList);
            JSONSerializer serializer = new JSONSerializer();
            finalViewPermissions = serializer.include("users").serialize(viewUserListJson);
        }

        // was a record id specified?
        logger.info("Submitted record id is " + submittedData.getId());
        if (submittedData.getId() != null) {
            MedicalRecord possibleExistingRecord = medicalRecordRepository.findById(submittedData.getId());
            RawRecord possibleExistingRawRecord = rawRecordRepository.findById(submittedData.getId());
            Boolean recordExists = (possibleExistingRecord != null) ? true : false;
            Boolean rawRecordExists = (possibleExistingRawRecord != null) ? true : false;

            if (recordExists || rawRecordExists) {
                logger.error("Cannot create raw record due to recordExists=" + recordExists + " and rawRecordExists=" + rawRecordExists
                        + ". You cannot create *new* records with a specific id if records already exist with that id.");
            }
            else {
                logger.info("Creating records with id " + submittedData.getId());
                MedicalRecordWithoutAutoId savedMedicalRecord = medicalRecordWithoutAutoIdRepository.save(new MedicalRecordWithoutAutoId(submittedData.getId(),
                        "Raw",
                        new Date(),
                        currentUser,
                        submittedData.getPatientUsername(),
                        finalEditPermissions,
                        finalViewPermissions));
                logger.info("Created  MedicalRecord with id " + savedMedicalRecord.getId());

                // create the raw record
                RawRecord savedRawRecord = rawRecordRepository.save(new RawRecord(submittedData.getId(),
                        submittedData.getDescription(),
                        fileAsBytes,
                        fileByteLength));
                logger.info("Created RawRecord with id " + savedRawRecord.getId());
                resultString = "SUCCESS";
            }
        }
        else {
            try {
                // create the record
                MedicalRecord savedMedicalRecord = medicalRecordRepository.save(new MedicalRecord("Raw",
                        new Date(),
                        currentUser,
                        submittedData.getPatientUsername(),
                        finalEditPermissions,
                        finalViewPermissions));
                logger.info("Created  MedicalRecord with id " + savedMedicalRecord.getId());

                // create the raw record
                // Use id auto assigned by db to MedicalRecord for rawRecord
                RawRecord savedRawRecord = rawRecordRepository.save(new RawRecord(savedMedicalRecord.getId(),
                        submittedData.getDescription(),
                        fileAsBytes,
                        fileByteLength));
                logger.info("Created RawRecord with id " + savedRawRecord.getId());


                resultString = "SUCCESS";
                logger.info("Created raw record for patient " + submittedData.getPatientUsername());
            } catch (Exception e) {
                logger.error("Failure creating raw record for patient " + submittedData.getPatientUsername());
                e.printStackTrace();
            }
        }
        ResultValue result = new ResultValue();
        result.setResult(resultString);
        logger.info("Authenticated user " + currentUser + " completed execution of service /addRawRecord");
        return result;
    }

    // 5.13 /createCorrespondenceRecord
    @Secured({"ROLE_DOCTOR","ROLE_PATIENT"})
    @ApiOperation(value = "Add a Correspondence Record to the database.",
            notes = "When createCorrespondenceRecord is successfully exercised, the result SHALL be a new Correspondance Record with valid non-null values added to the database. The createCorrespondenceRecord service SHALL only be accessible to users with the Doctor role and patient role.")
    @RequestMapping(value = "/createCorrespondenceRecord", method = RequestMethod.POST)
    public ResultValue createCorrespondenceRecord(@RequestBody @Valid CorrespondenceRecordModel submittedData) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Authenticated user " + currentUser + " is starting execution of service /createCorrespondenceRecord");
        String resultString = "FAILURE";

        Doctor possibleExistingDoctor = doctorRepository.findByUsername(submittedData.getDoctorUsername());
        Boolean doctorExists = (possibleExistingDoctor != null) ? true : false;
        Patient possibleExistingPatient = patientRepository.findByUsername(submittedData.getPatientUsername());
        Boolean patientExists = (possibleExistingPatient != null) ? true : false;

        if (doctorExists && patientExists) {
            //Check if API user specified supplemental users for edit or view permission
            Boolean editUsersSubmitted = ((submittedData.getEdit() != null) && (submittedData.getEdit().size() > 0)) ? true : false;
            Boolean viewUsersSubmitted = ((submittedData.getView() != null) && (submittedData.getView().size() > 0)) ? true : false;

            // create a json object of the default edit users
            ArrayList<String> editUserList = new ArrayList<String>();
            // by default do not add any users
            //editUserList.add(currentUser);
            Map<String, Object> editUserListJson = new HashMap<String, Object>();


            // create a json object of the default view users
            ArrayList<String> viewUserList = new ArrayList<String>();
            // by default do not add any users
            //viewUserList.add(currentUser);
            //viewUserList.add(submittedData.getPatientUsername());
            Map<String, Object> viewUserListJson = new HashMap<String, Object>();


            String finalEditPermissions = "";
            String finalViewPermissions = "";

            if (editUsersSubmitted) {
                List<String> userSuppliedListOfUsersToGrantEdit = submittedData.getEdit();
                for (String username : userSuppliedListOfUsersToGrantEdit) {
                    User possibleUser = userRepository.findByUsername(username);
                    if ((possibleUser != null) && (StringUtils.isNotEmpty(possibleUser.getUsername()))) {
                        editUserList.add(username);
                    }
                }
                editUserListJson.put("users", editUserList);
                JSONSerializer serializer = new JSONSerializer();
                finalEditPermissions = serializer.include("users").serialize(editUserListJson);
            }
            else {
                editUserListJson.put("users", editUserList);
                JSONSerializer serializer = new JSONSerializer();
                finalEditPermissions = serializer.include("users").serialize(editUserListJson);
            }

            if (viewUsersSubmitted) {
                List<String> userSuppliedListOfUsersToGrantView = submittedData.getView();
                for (String username : userSuppliedListOfUsersToGrantView) {
                    User possibleUser = userRepository.findByUsername(username);
                    if ((possibleUser != null) && (StringUtils.isNotEmpty(possibleUser.getUsername()))) {
                        viewUserList.add(username);
                    }
                }
                viewUserListJson.put("users", viewUserList);
                JSONSerializer serializer = new JSONSerializer();
                finalViewPermissions = serializer.include("users").serialize(viewUserListJson);
            }
            else {
                viewUserListJson.put("users", viewUserList);
                JSONSerializer serializer = new JSONSerializer();
                finalViewPermissions = serializer.include("users").serialize(viewUserListJson);
            }

            // was a record id specified?
            logger.info("Submitted record id is " + submittedData.getId());
            if (submittedData.getId() != null) {
                MedicalRecord possibleExistingRecord = medicalRecordRepository.findById(submittedData.getId());
                CorrespondenceRecord possibleExistingCorrespondenceRecord = correspondenceRecordRepository.findByNoteId(submittedData.getId());
                Boolean recordExists = (possibleExistingRecord != null) ? true : false;
                Boolean CorrespondenceRecordExists = (possibleExistingCorrespondenceRecord != null) ? true : false;

                if (recordExists || CorrespondenceRecordExists) {
                    logger.error("Cannot create Patient Doctor Correspondence record due to recordExists=" + recordExists + " and diagnosisRecordExists=" + CorrespondenceRecordExists
                            + ". You cannot create *new* records with a specific id if records already exist with that id.");
                }
                else {
                    logger.info("Creating records with id " + submittedData.getId());
                    MedicalRecordWithoutAutoId savedMedicalRecord = medicalRecordWithoutAutoIdRepository.save(new MedicalRecordWithoutAutoId(submittedData.getId(),
                            "Patient Doctor Correspondence",
                            new Date(),
                            currentUser,
                            submittedData.getPatientUsername(),
                            finalEditPermissions,
                            finalViewPermissions));
                    logger.info("Created  MedicalRecord with id " + savedMedicalRecord.getId());

                    // create the Diagnosis record
                    CorrespondenceRecord savedCorrespondenceRecord = correspondenceRecordRepository.save(new CorrespondenceRecord(submittedData.getId(),
                            submittedData.getDoctorUsername()));
                    logger.info("Created  Patient Doctor Correspondence record with id " + savedCorrespondenceRecord.getId());
                    resultString = "SUCCESS";
                }

            }
            else {
                try {
                    // create the record
                    MedicalRecord savedMedicalRecord = medicalRecordRepository.save(new MedicalRecord("Patient Doctor Correspondence",
                            new Date(),
                            currentUser,
                            submittedData.getPatientUsername(),
                            finalEditPermissions,
                            finalViewPermissions));
                    logger.info("Created  MedicalRecord with id " + savedMedicalRecord.getId());

                    // create the Patient Doctor Correspondence record
                    // Use id auto assigned by db to MedicalRecord for examRecord
                    CorrespondenceRecord savedCorrespondenceRecord = correspondenceRecordRepository.save(new CorrespondenceRecord(savedMedicalRecord.getId(),
                            submittedData.getDoctorUsername()));
                    logger.info("Created  CorrespondenceRecord with id " + savedCorrespondenceRecord.getId());
                    resultString = "SUCCESS";
                    logger.info("Created CorrespondenceRecord  for doctor " + submittedData.getDoctorUsername());
                } catch (Exception e) {
                    logger.error("Failure creating CorrespondenceRecord for doctor " + submittedData.getDoctorUsername());
                    e.printStackTrace();
                }
            }
        }
        else {
            logger.error("Cannot create CorrespondenceRecord due to doctorExists=" + doctorExists + " and patientExists=" +patientExists + ". Both need to exist.");
        }
        ResultValue result = new ResultValue();
        result.setResult(resultString);
        logger.info("Authenticated user " + currentUser + " completed execution of service /createCorrespondenceRecord");
        return result;
    }
    //    end of correspondance record

    // 5.14 /AddCorrespondenceNote

    @Secured({"ROLE_DOCTOR","ROLE_PATIENT"})
    @ApiOperation(value = "Add a Correspondence Record to the database.",
            notes = "When createCorrespondenceRecord is successfully exercised, the result SHALL be a new Correspondance Record with valid non-null values added to the database. The createCorrespondenceRecord service SHALL only be accessible to users with the Doctor role and patient role.")
    @RequestMapping(value = "/addCorrespondenceNote", method = RequestMethod.POST)
    public ResultValue createCorrespondenceNote(@RequestBody @Valid CorrespondenceRecordModel submittedData) {

        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Authenticated user " + currentUser + " is starting execution of service /addCorrespondenceNote");
        String resultString = "FAILURE";

        MedicalRecord a = medicalRecordRepository.findById(submittedData.getId());
        Boolean record1 = (a != null) ? true : false;
        Boolean correctInfo=false;
        String temp1=a.getOwner();
        String temp2=a.getPatient();

        if ( currentUser.equals(temp1) || currentUser.equals(temp2))
        {
            correctInfo=true;
        }


        if (record1 && correctInfo) {

            // was a record id specified?
            logger.info("Submitted record id is " + submittedData.getId());
            if (submittedData.getId() != null) {
                    // create the Diagnosis record
                    CorrespondenceRecord savedCorrespondenceRecord = correspondenceRecordRepository.save(new CorrespondenceRecord(submittedData.getId(),
                            submittedData.getNote_date(), submittedData.getNote_text()));
                    logger.info("Created  Correspondence note with id " + savedCorrespondenceRecord.getId());
                    resultString = "SUCCESS";

            }
        }
        else {
            logger.error("Cannot create CorrespondenceNote");
        }
        ResultValue result = new ResultValue();
        result.setResult(resultString);
        logger.info("Authenticated user " + currentUser + " completed execution of service /addCorrespondenceNote");
        return result;
    }
    //    end of correspondance record




    // 5.15 /listRecords
    @Secured({"ROLE_USER"})
    @ApiOperation(value = "List all records the accessing user has permissions on.",
            notes = "When listRecords service is successfully exercised the server application SHALL return a list containing the Record ID, Record Type, and Record Date for all records that the accessing user is listed as either owner, on edit permissions list or view permissions list. The listRecords service SHALL be accessible to all users. \n")
    @RequestMapping(value = "/listRecords", method = RequestMethod.GET)
    public ArrayList<String> listRecords() {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Authenticated user " + currentUser + " is starting execution of service /listRecords");

        List<MedicalRecord> recordsAsOwner = medicalRecordRepository.findByOwner(currentUser);
        logger.info("Found " + recordsAsOwner.size() + " recordsAsOwner");
        //List<MedicalRecord> recordsAsPatient = medicalRecordRepository.findByPatient(currentUser);
        //logger.info("Found " + recordsAsPatient.size() + " recordsAsPatient");
        List<MedicalRecord> recordsAsViewer = medicalRecordRepository.findByViewContaining("\"" +currentUser + "\"");
        logger.info("Found " + recordsAsViewer.size() + " recordsAsViewer");
        List<MedicalRecord> recordsAsEditor = medicalRecordRepository.findByEditContaining("\"" +currentUser + "\"");
        logger.info("Found " + recordsAsEditor.size() + " recordsAsEditor");
        Set<MedicalRecord> myRecordsSet = new HashSet<MedicalRecord>(recordsAsOwner);
        //myRecordsSet.addAll(recordsAsPatient);
        myRecordsSet.addAll(recordsAsViewer);
        myRecordsSet.addAll(recordsAsEditor);
        logger.info("There are " + myRecordsSet.size() + " distinct records that I have access to.");

        ArrayList<String> recordSummaryList = new ArrayList<String>();
        for (MedicalRecord record : myRecordsSet) {
            String summary = record.getId() + "," + record.getRecord_type() + "," + record.getDate();
            recordSummaryList.add(summary);
        }

        logger.info("Authenticated user " + currentUser + " completed execution of service /listRecords");
        return recordSummaryList;
    }

    // 5.16 /viewRecord
    @Secured({"ROLE_USER"})
    @ApiOperation(value = "View a record.",
            notes = "When viewRecord service is successfully exercised the server application SHALL return the record corresponding to the record ID requested in the service call. The viewRecord service SHALL only return the record if the accessing user is listed on the records view permissions list or is the record owner. The viewRecord service SHALL only return a Diagnosis Record if the accessing user has Doctor Role.")
    @RequestMapping(value = "/viewRecord/{submittedId}", method = RequestMethod.GET)
    public SuperSetOfAllMedicalRecordTypes viewRecord(@PathVariable Integer submittedId) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean currentUserisDoctor = SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_DOCTOR"));
        boolean currentUserisInsuranceAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_INSURANCE_ADMIN"));
        boolean currentUserisMedicalAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MEDICAL_ADMIN"));
        boolean currentUserisPatient = SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PATIENT"));
        boolean currentUserisNurse = SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_NURSE"));
        logger.info("Authenticated user " + currentUser + " is starting execution of service /viewRecord");
        logger.info("Authenticated user " + currentUser + " is a doctor? answer: " + currentUserisDoctor);
        SuperSetOfAllMedicalRecordTypes resultRecord = new SuperSetOfAllMedicalRecordTypes();

        // first find the base MedicalRecord
        MedicalRecord record = medicalRecordRepository.findById(submittedId);
        String owner = record.getOwner();
        logger.info("Owner of MedicalRecord " + submittedId + " is " + owner);

        // Determine the listOfUsersThatHaveViewPermissionsToThisRecord
        // parse the list of viewers stored in the db as a json list to a java arraylist
        String viewersJsonList = record.getView();
        List<String> listOfUsersThatHaveViewPermissionsToThisRecord = new ArrayList<String>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> mapObject = mapper.readValue(viewersJsonList,
                    new TypeReference<Map<String, Object>>() {
                    });
            listOfUsersThatHaveViewPermissionsToThisRecord = (List<String>) mapObject.get("users");
        }
        catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String viewer : listOfUsersThatHaveViewPermissionsToThisRecord) {
            logger.info("record " + submittedId + " viewers include " + viewer);
        }

        // check if currentuser is the owner or is in the view list
        if (currentUser.equals(owner) || listOfUsersThatHaveViewPermissionsToThisRecord.contains(currentUser)) {

            // user is owner or in view list

            // It looks like this user can view this record, now lets determine the record type
            // and populate the SuperSetOfAllMedicalRecordTypes we are returning with the data
            // from the base MedicalRecord class and the Subtype.

            // set return values for base MedicalRecord
            resultRecord.setMedicalRecordId(record.getId());
            resultRecord.setMedicalRecordDate(record.getDate());
            resultRecord.setMedicalRecordEdit(record.getEdit());
            resultRecord.setMedicalRecordOwner(record.getOwner());
            resultRecord.setMedicalRecordPatient(record.getPatient());
            resultRecord.setMedicalRecordView(record.getView());
            resultRecord.setMedicalRecordRecord_type(record.getRecord_type());

            // set return values that are dependent on record subtype
            String recordSubType = record.getRecord_type();
            switch (recordSubType) {
                case "Doctor Exam":
                    // set return values for DoctorExamRecord
                    DoctorExamRecord doctorExamRecord = doctorExamRecordRepository.findById(record.getId());
                    if (doctorExamRecord != null) {
                        resultRecord.setDoctorExamRecordDoctor(doctorExamRecord.getDoctor());
                        resultRecord.setDoctorExamRecordExamDate(doctorExamRecord.getDate());
                        resultRecord.setDoctorExamRecordNotes(doctorExamRecord.getNotes());
                    }
                    break;

                case "Test Result":
                    // set return values for TestResultRecord
                    TestResultRecord testResultRecord = testResultRecordRepository.findById(record.getId());
                    if (testResultRecord != null) {
                        resultRecord.setTestResultRecorddate(testResultRecord.getDate());
                        resultRecord.setTestResultRecordDoctor(testResultRecord.getDoctor());
                        resultRecord.setTestResultRecordLab(testResultRecord.getLab());
                        resultRecord.setTestResultRecordnotes(testResultRecord.getNotes());
                    }

                    break;

                case "Diagnosis":
                    // set return values for DiagnosisRecord
                    DiagnosisRecord diagnosisRecord = diagnosisRecordRepository.findById(record.getId());

                    resultRecord.setDiagnosisRecordDate(diagnosisRecord.getDate());
                    resultRecord.setDiagnosisRecordDoctor(diagnosisRecord.getDoctor());
                    resultRecord.setDiagnosisRecordDiagnosis(diagnosisRecord.getDiagnosis());
                    break;

                case "Insurance Claim":
                    // set return values for InsuranceClaimRecord
                    InsuranceClaimRecord insuranceClaimRecord = insuranceClaimRecordRepository.findById(record.getId());
                    resultRecord.setInsuranceClaimRecordMadmin(insuranceClaimRecord.getMadmin());
                    resultRecord.setInsuranceClaimRecordStatus(insuranceClaimRecord.getStatus());
                    resultRecord.setInsuranceClaimRecordClaimDate(insuranceClaimRecord.getDate());
                    resultRecord.setInsuranceClaimRecordClaimAmount(insuranceClaimRecord.getAmount());

                    break;
                case "Patient Doctor Correspondence":
                    // set return values for CorrespondenceRecord
                    CorrespondenceRecord[] correspondenceRecordList = correspondenceRecordRepository.findCorrespondenceRecordsById(record.getId());
                    resultRecord.setCorrespondenceRecordList(correspondenceRecordList);
                    break;

                case "Raw":
                    // set return values for RawRecord
                    RawRecord rawRecord = rawRecordRepository.findById(record.getId());
                    resultRecord.setRawRecordDescription(rawRecord.getDescription());
                    byte[] fileAsBytes = rawRecord.getFile();
                    byte[] trimmedFileAsBytes = Arrays.copyOfRange(fileAsBytes, 0, (rawRecord.getLength() -1));
                    resultRecord.setRawRecordBase64EncodedBinary(Base64.getEncoder().encodeToString(trimmedFileAsBytes));
                    break;

                default:
                    logger.error("Record type not found: " + record.getRecord_type());
            }

        }
        else {
            logger.warn(currentUser + " is neither the owner nor has view permissions to this record");
        }

        logger.info("Authenticated user " + currentUser + " completed execution of service /viewRecord");
        return resultRecord;
    }

    // 5.17 editRecordPerm
    @Secured({"ROLE_ASSIGN_PERMISSIONS"})
    @ApiOperation(value = "Edit a record’s permissions.",
            notes = "When editRecordPerm is successfully exercised, either a record’s Edit Permissions or View Permissions list fields SHALL be changed. The editRecordPerm service SHALL only change permissions to the record specified in the call to the service. The editRecordPerm service SHALL only change permissions if the calling user is listed in the records Edit Permissions list field or has Edit Record Access permissions.")
    @RequestMapping(value = "/editRecordPerm", method = RequestMethod.POST)
    public ResultValue editRecordPerm(@RequestBody @Valid CustomRecordPermissionsModel submittedData) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Authenticated user " + currentUser + " is starting execution of service /editRecordPerm");
        String resultString = "FAILURE";

        MedicalRecord medRecord = medicalRecordRepository.findById(submittedData.getId());
        Boolean recordExists = ((medRecord !=null) && (medRecord.getId().equals(submittedData.getId()))) ? true : false;

        if (recordExists) {

            Boolean editListProvided = ( (submittedData.getEditors() != null) && submittedData.getEditors().size() > 0) ? true : false;
            Boolean viewListProvided = ( (submittedData.getViewers() != null) && submittedData.getViewers().size() > 0) ? true : false;
            String finalEditPermissions = "";
            String finalViewPermissions = "";

            if (editListProvided) {
                // verify these users exist and if so then replace the edit list with this list
                List<String> userSuppliedListOfUsersToGrantEdit = submittedData.getEditors();
                ArrayList<String> editUserList = new ArrayList<String>();
                Map<String, Object> editUserListJson = new HashMap<String, Object>();
                for (String username : userSuppliedListOfUsersToGrantEdit) {
                    User possibleUser = userRepository.findByUsername(username);
                    if ((possibleUser != null) && (StringUtils.isNotEmpty(possibleUser.getUsername()))) {
                        editUserList.add(username);
                    }
                }
                editUserListJson.put("users", editUserList);
                JSONSerializer serializer = new JSONSerializer();
                finalEditPermissions = serializer.include("users").serialize(editUserListJson);
                medRecord.setEdit(finalEditPermissions);
            }

            if (viewListProvided) {
                // verify these users exist and if so then replace the view list with this list
                List<String> userSuppliedListOfUsersToGrantView = submittedData.getViewers();
                ArrayList<String> viewUserList = new ArrayList<String>();
                Map<String, Object> viewUserListJson = new HashMap<String, Object>();
                for (String username : userSuppliedListOfUsersToGrantView) {
                    User possibleUser = userRepository.findByUsername(username);
                    if ((possibleUser != null) && (StringUtils.isNotEmpty(possibleUser.getUsername()))) {
                        viewUserList.add(username);
                    }
                }
                viewUserListJson.put("users", viewUserList);
                JSONSerializer serializer = new JSONSerializer();
                finalViewPermissions = serializer.include("users").serialize(viewUserListJson);
                medRecord.setView(finalViewPermissions);
            }

            if (editListProvided || viewListProvided) {
                MedicalRecord savedMedicalRecord = medicalRecordRepository.save(medRecord);
                logger.info("Authenticated user " + currentUser + " updated permissions on record " + savedMedicalRecord.getId());
                resultString = "SUCCESS";
            }
        }
        ResultValue result = new ResultValue();
        result.setResult(resultString);
        logger.info("Authenticated user " + currentUser + " completed execution of service /editRecordPerm");
        return result;
    }


}
