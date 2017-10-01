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

    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public String process(){

        repository.save(Arrays.asList(new Record("Jack", "Smith"),
                new Record("Adam", "Johnson"),
                new Record("Kim", "Smith"),
                new Record("David", "Williams"),
                new Record("Peter", "Davis")));

        return "Done";
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public String findAll(){

        String result = "";

        for(Record record : repository.findAll()){
            result += record + "</br>";
        }

        return result;
    }

    @RequestMapping(value = "/findbyrecordID", method = RequestMethod.GET)
    public String findByRecordID(@RequestParam("recordID") long recordID){
        String result = "";
        result = repository.findOne(recordID).toString();
        return result;
    }

    @RequestMapping(value = "/findbyowner", method = RequestMethod.GET)
    public String fetchDataByOwner(@RequestParam("owner") String owner){
        String result = "";

        for(Record record: repository.findByOwner(owner)){
            result += record + "</br>";
        }
        return result;
    }

    @RequestMapping(value = "/findbypatient", method = RequestMethod.GET)
    public String fetchDataByPatient(@RequestParam("patient") String patient){
        String result = "";

        for(Record record: repository.findByPatient(patient)){
            result += record + "</br>";
        }
        return result;
    }

}