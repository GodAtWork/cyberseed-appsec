package edu.syr.cyberseed.sage.server.controllers;

import java.util.Arrays;

import edu.syr.cyberseed.sage.server.entities.Patient;
import edu.syr.cyberseed.sage.server.entities.Record;
import edu.syr.cyberseed.sage.server.entities.ResultValue;
import edu.syr.cyberseed.sage.server.entities.User;
import edu.syr.cyberseed.sage.server.entities.models.PatientUserModel;
import edu.syr.cyberseed.sage.server.repositories.PatientRepository;
import edu.syr.cyberseed.sage.server.repositories.RecordRepository;
import edu.syr.cyberseed.sage.server.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class RecordController {

    @Autowired
    RecordRepository recordRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PatientRepository patientRepository;
    //COMMENTED
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(RecordController.class);

    @RequestMapping(value = "/createPatient", method = RequestMethod.POST)
     public ResultValue createPatient(@RequestBody @Valid PatientUserModel user) {
        String resultString = "FAILURE";
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        try {
            // create the User record
            userRepository.save(Arrays.asList(new User(user.getUsername(), user.getPassword(),user.getFname(),user.getLname())));
            // create the Patient record
            patientRepository.save(Arrays.asList(new Patient(user.getUsername(), user.getDob(),user.getSsn(),user.getAddress())));
            resultString = "SUCCESS";
            logger.info("Created patient user " + user.getUsername());
        } catch (Exception e) {
            logger.error("Failure creating patient user " + user.getUsername());
            e.printStackTrace();
        }

        ResultValue result = new ResultValue();
        result.setResult(resultString);
        return result;
    }


//DUMMY EXAMPLES
    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public String process() {

        recordRepository.save(Arrays.asList(new Record("Jack", "Smith"),
                new Record("Adam", "Johnson"),
                new Record("Kim", "Smith"),
                new Record("David", "Williams"),
                new Record("Peter", "Davis")));

        return "Done";
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public String findAll() {

        String result = "";

        for (Record record : recordRepository.findAll()) {
            result += record + "</br>";
        }

        return result;
    }

    @RequestMapping(value = "/findbyrecordID", method = RequestMethod.GET)
    public String findByRecordID(@RequestParam("recordID") long recordID) {
        String result = "";
        result = recordRepository.findOne(recordID).toString();
        return result;
    }

    @RequestMapping(value = "/findbyowner", method = RequestMethod.GET)
    public String fetchDataByOwner(@RequestParam("owner") String owner) {
        String result = "";

        for (Record record : recordRepository.findByOwner(owner)) {
            result += record + "</br>";
        }
        return result;
    }

    @RequestMapping(value = "/findbypatient", method = RequestMethod.GET)
    public String fetchDataByPatient(@RequestParam("patient") String patient) {
        String result = "";

        for (Record record : recordRepository.findByPatient(patient)) {
            result += record + "</br>";
        }
        return result;
    }

}
