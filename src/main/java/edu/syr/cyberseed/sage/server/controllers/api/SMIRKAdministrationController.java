package edu.syr.cyberseed.sage.server.controllers.api;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.syr.cyberseed.sage.server.entities.*;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.*;
import java.util.*;



@RestController
public class SMIRKAdministrationController {
    String filename="";
    String ip="";
    String user="";
    String pass="";
    String answer="no";
    private static final Logger logger = LoggerFactory.getLogger(SMIRKAdministrationController.class);

    @Secured({"ROLE_SYSTEM_ADMIN"})
    @RequestMapping(value = "/loadBackupCfg", method = RequestMethod.POST)
    public ResultValue loadBackupCfg(@RequestBody BackupConfig submittedData) throws FileNotFoundException, UnsupportedEncodingException {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Authenticated user " + currentUser + " is starting execution of service /loadBackupCfg");
        String resultString = "FAILURE";

        ip = submittedData.getOffsiteServerIp();
        user = submittedData.getOffsiteServerUsername();
        pass = submittedData.getOffsiteServerPassword();
        filename = submittedData.getOffsiteServerFilename();

        //TODO
        logger.info("Authenticated user " + ip + " completed execution of service /loadBackupCfg");
        logger.info("Authenticated user " + user + " completed execution of service /loadBackupCfg");
        logger.info("Authenticated user " + pass + " completed execution of service /loadBackupCfg");
        logger.info("Authenticated user " + filename + " completed execution of service /loadBackupCfg");
        logger.info("1");
        PrintWriter writer = new PrintWriter("db_backup_2017.cfg", "UTF-8");
        System.out.println("file created");
        logger.info("2");
        writer.println(ip);
        writer.println(user);
        writer.println(pass);
        writer.close();
        logger.info("3");
        ResultValue result = new ResultValue();
        result.setResult(resultString);
        logger.info("Authenticated user " + currentUser + " completed execution of service /loadBackupCfg");
        return result;
    }

    @Secured({"ROLE_SYSTEM_ADMIN"})
    @RequestMapping(value = "/getBackupCfg", method = RequestMethod.GET)
    public BackupConfig viewRecord() {

        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        BackupConfig resultRecord = new BackupConfig();
        // first find the base MedicalRecord
        BackupConfig record = new BackupConfig();
        String owner = filename;


        // check if currentuser is the owner or is in the view list
        if (filename.contains("db_backup_2017")) {
            try (BufferedReader br = new BufferedReader(new FileReader("db_backup_2017.cfg"))) {
                answer="yes";
                resultRecord.setAnswer(answer);
                resultRecord.setOffsiteServerIp(br.readLine());
                resultRecord.setOffsiteServerUsername(br.readLine());
                resultRecord.setOffsiteServerPassword(br.readLine());
            } catch (IOException e) {
                answer="no";
                resultRecord.setAnswer(answer);
                System.out.println("File does not exist");
            }

            logger.info("setting yes ");

        }
        else {
            resultRecord.setAnswer(answer);
            logger.info("setting no ");
        }

        logger.info("done");
        return resultRecord;
    }

    @Secured({"ROLE_SYSTEM_ADMIN"})
    @RequestMapping(value = "/dumpDb", method = RequestMethod.GET)
    public String dump() {
    String line="";
    String save="";

        String command = "mysqldump --all-databases --xml --user root --password=appsec";
        logger.info("command");

        Process proc = null;
        try {
            proc = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Read the output

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String abc="";
        try {
            while((line = reader.readLine()) != null) {
                abc=abc+reader.readLine()+"\n";
            }
            logger.info(abc);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            proc.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("sending");
        return abc;


    }

}

//mysqldump --all-databases --xml --user root --password=appsec  > abc.xml

