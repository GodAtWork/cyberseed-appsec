package edu.syr.cyberseed.sage.server.controllers.api;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.syr.cyberseed.sage.server.entities.*;
import edu.syr.cyberseed.sage.server.entities.models.DoctorExamRecordModel;
import edu.syr.cyberseed.sage.server.repositories.*;
import io.swagger.annotations.ApiOperation;
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
    PermissionsRepository permissionListRepository;
    @Autowired
    DoctorExamRecordRepository doctorExamRecordRepository;

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
                            "{\"users\":[\"" + currentUser + "\"]}",
                            "{\"users\":[\"" + currentUser + "\",\"" + submittedData.getPatientUsername() + "\"]}"));
                    logger.info("Created  MedicalRecord with id " + savedMedicalRecord.getId());

                    // create the Doctor exam record
                    DoctorExamRecord savedDoctorExamRecord = doctorExamRecordRepository.save(new DoctorExamRecord(submittedData.getId(),
                            submittedData.getDoctorUsername(),
                            submittedData.getExamDate(),
                            submittedData.getNotes()));
                    logger.info("Created  DoctorExamRecord with id " + savedDoctorExamRecord.getId());
                }

            }
            else {
                try {
                    // create the record
                    MedicalRecord savedMedicalRecord = medicalRecordRepository.save(new MedicalRecord("Doctor Exam",
                            new Date(),
                            currentUser,
                            submittedData.getPatientUsername(),
                            "{\"users\":[\"" +currentUser + "\"]}",
                            "{\"users\":[\"" + currentUser + "\",\"" + submittedData.getPatientUsername() + "\"]}"));
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

    // 5.15 /listRecords
    @Secured({"ROLE_USER"})
    @ApiOperation(value = "List all records the accessing user has permissions on.",
            notes = "When listRecords service is successfully exercised the server application SHALL return a list containing the Record ID, Record Type, and Record Date for all records that the accessing user is listed as either owner, on edit permissions list or view permissions list. The listRecords service SHALL be accessible to all users. \n")
    @RequestMapping(value = "/listRecords", method = RequestMethod.GET)
    public ArrayList<String> listRecords() {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Authenticated user " + currentUser + " is starting execution of service /listRecords");
        String resultString = "FAILURE";

        List<MedicalRecord> recordsAsOwner = medicalRecordRepository.findByOwner(currentUser);
        logger.info("Found " + recordsAsOwner.size() + " recordsAsOwner");
        List<MedicalRecord> recordsAsPatient = medicalRecordRepository.findByPatient(currentUser);
        logger.info("Found " + recordsAsPatient.size() + " recordsAsPatient");
        List<MedicalRecord> recordsAsViewer = medicalRecordRepository.findByViewContaining("\"" +currentUser + "\"");
        logger.info("Found " + recordsAsViewer.size() + " recordsAsViewer");
        List<MedicalRecord> recordsAsEditor = medicalRecordRepository.findByEditContaining("\"" +currentUser + "\"");
        logger.info("Found " + recordsAsEditor.size() + " recordsAsEditor");
        Set<MedicalRecord> myRecordsSet = new HashSet<MedicalRecord>(recordsAsOwner);
        myRecordsSet.addAll(recordsAsPatient);
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
    public MedicalRecord viewRecord(@PathVariable Integer submittedId) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean currentUserisDoctor = SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_DOCTOR"));
        logger.info("Authenticated user " + currentUser + " is starting execution of service /viewRecord");
        logger.info("Authenticated user " + currentUser + " is a doctor? answer: " + currentUserisDoctor);
        String resultString = "FAILURE";
        MedicalRecord resultRecord = null;
        MedicalRecord record = medicalRecordRepository.findById(submittedId);
        String owner = record.getOwner();
        logger.info("record " + submittedId + " owner is " + owner);
        // parse the list of viewers stored in the db as a json list to a java arraylist
        String viewersJsonList = record.getView();
        List<String> viewerList = new ArrayList<String>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> mapObject = mapper.readValue(viewersJsonList,
                    new TypeReference<Map<String, Object>>() {
                    });
            viewerList = (List<String>) mapObject.get("users");
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
        for (String viewer : viewerList) {
            logger.info("record " + submittedId + " viewers include " + viewer);
        }

        // check if current is owner or in view list
        if (currentUser.equals(owner) || viewerList.contains(currentUser)) {
            if (record.getRecord_type().equals("Diagnosis Record") && !currentUserisDoctor) {
                logger.warn(currentUser + " is not a doctor so cannot view a Diagnosis Record.");
            }
            else {
                // user is owner or in view list
                // if the records is a diag record the user is a doctor
                // set the record object we will return to the retrieved record
                resultRecord = record;
            }
        }
        else {
            logger.warn(currentUser + " is neither the owner nor has view permissions to this record");
        }

        logger.info("Authenticated user " + currentUser + " completed execution of service /viewRecord");
        return resultRecord;
    }

}
