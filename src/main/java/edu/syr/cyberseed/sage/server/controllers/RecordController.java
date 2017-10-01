package edu.syr.cyberseed.sage.server.controllers;

import java.util.Arrays;

import edu.syr.cyberseed.sage.server.entities.Record;
import edu.syr.cyberseed.sage.server.repositories.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
public class RecordController {

    @Autowired
    RecordRepository repository;

    @RequestMapping(value = "/createPatient", method = RequestMethod.POST)
    public void fetchDataByOwner(@RequestParam("owner") String owner) {
    }

    @RequestMapping(value = "/createDoctor", method = RequestMethod.POST)
    public void fetchDataByOwner(@RequestParam("owner") String owner) {
    }

    @RequestMapping(value = "/createNurse", method = RequestMethod.POST)
    public void fetchDataByOwner(@RequestParam("owner") String owner) {
    }

    @RequestMapping(value = "/createSysAdmin", method = RequestMethod.POST)
    public void fetchDataByOwner(@RequestParam("owner") String owner) {
    }

    @RequestMapping(value = "/createMedAdmin", method = RequestMethod.POST)
    public void fetchDataByOwner(@RequestParam("owner") String owner) {
    }

    @RequestMapping(value = "/createInsAdmin", method = RequestMethod.POST)
    public void fetchDataByOwner(@RequestParam("owner") String owner) {
    }

    @RequestMapping(value = "/editPerm", method = RequestMethod.POST)
    public void fetchDataByOwner(@RequestParam("owner") String owner) {
    }

    @RequestMapping(value = "/addDoctorExamRecord", method = RequestMethod.POST)
    public void fetchDataByOwner(@RequestParam("owner") String owner) {
    }

    @RequestMapping(value = "/addTestResult", method = RequestMethod.POST)
    public void fetchDataByOwner(@RequestParam("owner") String owner) {
    }

    @RequestMapping(value = "/addDiagnosisRecord", method = RequestMethod.POST)
    public void fetchDataByOwner(@RequestParam("owner") String owner) {
    }

    @RequestMapping(value = "/addInsuranceClaimRecord", method = RequestMethod.POST)
    public void fetchDataByOwner(@RequestParam("owner") String owner) {
    }

    @RequestMapping(value = "/addRawRecord", method = RequestMethod.POST)
    public void fetchDataByOwner(@RequestParam("owner") String owner) {
    }

    @RequestMapping(value = "/createCorrespondenceRecord", method = RequestMethod.POST)
    public void fetchDataByOwner(@RequestParam("owner") String owner) {
    }

    @RequestMapping(value = "/addCorrespondenceNote", method = RequestMethod.POST)
    public void fetchDataByOwner(@RequestParam("owner") String owner) {
    }

    @RequestMapping(value = "/listRecords", method = RequestMethod.GET)
    public void fetchDataByOwner(@RequestParam("owner") String owner) {
    }

    @RequestMapping(value = "/viewRecord", method = RequestMethod.GET)
    public void fetchDataByOwner(@RequestParam("owner") String owner) {
    }

    @RequestMapping(value = "/editRecordPerm", method = RequestMethod.POST)
    public void fetchDataByOwner(@RequestParam("owner") String owner) {
    }

    @RequestMapping(value = "/editPatient", method = RequestMethod.POST)
    public void fetchDataByOwner(@RequestParam("owner") String owner) {
    }

    @RequestMapping(value = "/editDoctor", method = RequestMethod.POST)
    public void fetchDataByOwner(@RequestParam("owner") String owner) {
    }

    @RequestMapping(value = "/editNurse", method = RequestMethod.POST)
    public void fetchDataByOwner(@RequestParam("owner") String owner) {
    }

    @RequestMapping(value = "/editSysAdmin", method = RequestMethod.POST)
    public void fetchDataByOwner(@RequestParam("owner") String owner) {
    }

    @RequestMapping(value = "/editInsAdmin", method = RequestMethod.POST)
    public void fetchDataByOwner(@RequestParam("owner") String owner) {
    }

    @RequestMapping(value = "/editMedAdmin", method = RequestMethod.POST)
    public void fetchDataByOwner(@RequestParam("owner") String owner) {
    }

    @RequestMapping(value = "/viewPatientProfile", method = RequestMethod.GET)
    public void fetchDataByOwner(@RequestParam("owner") String owner) {
    }

    @RequestMapping(value = "/viewRecoveryPhrase", method = RequestMethod.GET)
    public void fetchDataByOwner(@RequestParam("owner") String owner) {
    }

    @RequestMapping(value = "/removeUserProfile", method = RequestMethod.POST)
    public void fetchDataByOwner(@RequestParam("owner") String owner) {
    }

//DUMMY EXAMPLES
    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public String process() {

        repository.save(Arrays.asList(new Record("Jack", "Smith"),
                new Record("Adam", "Johnson"),
                new Record("Kim", "Smith"),
                new Record("David", "Williams"),
                new Record("Peter", "Davis")));

        return "Done";
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public String findAll() {

        String result = "";

        for (Record record : repository.findAll()) {
            result += record + "</br>";
        }

        return result;
    }

    @RequestMapping(value = "/findbyrecordID", method = RequestMethod.GET)
    public String findByRecordID(@RequestParam("recordID") long recordID) {
        String result = "";
        result = repository.findOne(recordID).toString();
        return result;
    }

    @RequestMapping(value = "/findbyowner", method = RequestMethod.GET)
    public String fetchDataByOwner(@RequestParam("owner") String owner) {
        String result = "";

        for (Record record : repository.findByOwner(owner)) {
            result += record + "</br>";
        }
        return result;
    }

    @RequestMapping(value = "/findbypatient", method = RequestMethod.GET)
    public String fetchDataByPatient(@RequestParam("patient") String patient) {
        String result = "";

        for (Record record : repository.findByPatient(patient)) {
            result += record + "</br>";
        }
        return result;
    }

}
