package edu.syr.cyberseed.sage.server.controllers.api;

import edu.syr.cyberseed.sage.server.entities.BackupConfig;
import edu.syr.cyberseed.sage.server.entities.ResultValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class SMIRKAdministrationController {

    private static final Logger logger = LoggerFactory.getLogger(SMIRKAdministrationController.class);

    @Secured({"ROLE_SYSTEM_ADMIN"})
    @RequestMapping(value = "/loadBackupCfg", method = RequestMethod.POST)
    public ResultValue loadBackupCfg(@RequestBody @Valid BackupConfig submittedData) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Authenticated user " + currentUser + " is starting execution of service /loadBackupCfg");
        String resultString = "FAILURE";

        String ip = submittedData.getOffsiteServerIp();
        String user = submittedData.getOffsiteServerUsername();
        String pass = submittedData.getOffsiteServerPassword();

        //TODO
        // write the three submittedData fields to a file on the the app server

        ResultValue result = new ResultValue();
        result.setResult(resultString);
        logger.info("Authenticated user " + currentUser + " completed execution of service /loadBackupCfg");
        return result;
    }

    @Secured({"ROLE_SYSTEM_ADMIN"})
    @RequestMapping(value = "/getBackupCfg", method = RequestMethod.GET)
    public BackupConfig getBackupCfg() {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Authenticated user " + currentUser + " is starting execution of service /getBackupCfg");

        BackupConfig config = new BackupConfig();

        //TODO
        // read three config data values from config file on app server

        //config.setOffsiteServerIp(ffff);
        //config.setOffsiteServerPassword(ffff);
        //config.setOffsiteServerUsername(ffff);

        logger.info("Authenticated user " + currentUser + " completed execution of service /getBackupCfg");
        return config;
    }

}
