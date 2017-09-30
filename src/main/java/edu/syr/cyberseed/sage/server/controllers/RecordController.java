package edu.syr.cyberseed.sage.server.controllers;

import java.util.Arrays;

import edu.syr.cyberseed.sage.server.entities.Record;
import edu.syr.cyberseed.sage.server.repositories.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RecordController {

    @Autowired
    RecordRepository repository;

    @GetMapping("/save")
    public String process(){

        repository.save(Arrays.asList(new Record("Jack", "Smith"),
                new Record("Adam", "Johnson"),
                new Record("Kim", "Smith"),
                new Record("David", "Williams"),
                new Record("Peter", "Davis")));

        return "Done";
    }


    @GetMapping("/findAll")
    public String findAll(){

        String result = "";

        for(Record record : repository.findAll()){
            result += record + "</br>";
        }

        return result;
    }

    @GetMapping("/findbyrecordID")
    public String findByRecordID(@RequestParam("recordID") long recordID){
        String result = "";
        result = repository.findOne(recordID).toString();
        return result;
    }

    @GetMapping("/findbyowner")
    public String fetchDataByOwner(@RequestParam("owner") String owner){
        String result = "";

        for(Record record: repository.findByOwner(owner)){
            result += record + "</br>";
        }
        return result;
    }

    @GetMapping("/findbypatient")
    public String fetchDataByPatient(@RequestParam("patient") String patient){
        String result = "";

        for(Record record: repository.findByPatient(patient)){
            result += record + "</br>";
        }
        return result;
    }

}