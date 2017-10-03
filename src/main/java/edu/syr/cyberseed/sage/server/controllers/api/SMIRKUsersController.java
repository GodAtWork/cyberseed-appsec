package edu.syr.cyberseed.sage.server.controllers.api;

import java.util.*;

import edu.syr.cyberseed.sage.server.entities.*;
import edu.syr.cyberseed.sage.server.entities.models.CustomPermissionsModel;
import edu.syr.cyberseed.sage.server.entities.models.DoctorUserModel;
import edu.syr.cyberseed.sage.server.entities.models.PatientUserModel;
import edu.syr.cyberseed.sage.server.repositories.*;
import flexjson.JSONSerializer;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class SMIRKUsersController {

    @Autowired
    ConfigurableApplicationContext context;
    @Autowired
    MedicalRecordRepository medicalRecordRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    PermissionsRepository permissionListRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(SMIRKUsersController.class);

    // 5.1 /createPatient
    @Secured({"ROLE_ADD_PATIENT"})
    @ApiOperation(value = "Add a patient user profile to the system.",
            notes = "When createPatient service is successfully exercised, the result SHALL be a new Patient User Profile with default permissions and data for all fields in the Patient User Profile type with valid non-null values added to the database.  The createPatient service SHALL only be accessible to a user with Add Patient permission.")
    @RequestMapping(value = "/createPatient", method = RequestMethod.POST)
     public ResultValue createPatient(@RequestBody @Valid PatientUserModel submittedData) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Authenticated user " + currentUser + " is starting execution of service /createPatient");
        String resultString = "FAILURE";

        User possibleExistingUser = userRepository.findByUsername(submittedData.getUsername());
        if ((possibleExistingUser == null) || (StringUtils.isEmpty(possibleExistingUser.getUsername()))) {
            submittedData.setPassword(bCryptPasswordEncoder.encode(submittedData.getPassword()));

            //Create a json list of roles for a user of this type
            String roles;
            ArrayList<String> roleList = new ArrayList<String>();
            roleList.add("ROLE_USER");
            roleList.add("ROLE_PATIENT");
            Map<String, Object> rolesJson = new HashMap<String, Object>();
            rolesJson.put("roles", roleList);
            JSONSerializer serializer = new JSONSerializer();
            roles = serializer.include("roles").serialize(rolesJson);

            logger.info("Adding user " + submittedData.getUsername() + " with roles " + roles);
            try {
                // create the User record
                userRepository.save(Arrays.asList(new User(submittedData.getUsername(),
                        submittedData.getPassword(),
                        submittedData.getFname(),
                        submittedData.getLname(),
                        roles,
                        null,
                        null)));
                // create the Patient record
                patientRepository.save(Arrays.asList(new Patient(submittedData.getUsername(), submittedData.getDob(), submittedData.getSsn(), submittedData.getAddress())));
                resultString = "SUCCESS";
                logger.info("Created patient user " + submittedData.getUsername());
            } catch (Exception e) {
                logger.error("Failure creating patient user " + submittedData.getUsername());
                e.printStackTrace();
            }
        }
        ResultValue result = new ResultValue();
        result.setResult(resultString);
        logger.info("Authenticated user " + currentUser + " completed execution of service /createPatient");
        return result;
    }

    // 5.2 /CreateDoctor
    @Secured({"ROLE_ADD_DOCTOR"})
    @ApiOperation(value = "Add a doctor user profile to the system.",
            notes = "When createDoctor service is successfully exercised, the result SHALL be a new Doctor User Profile with default permissions and data for all fields in the Doctor User Profile type with valid non-null values added to the database.  The createDoctor service SHALL only be accessible to a user with Add Doctor permission.")
    @RequestMapping(value = "/createDoctor", method = RequestMethod.POST)
    public ResultValue createDoctor(@RequestBody @Valid DoctorUserModel submittedData) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Authenticated user " + currentUser + " is starting execution of service /createDoctor");
        String resultString = "FAILURE";


        User possibleExistingUser = userRepository.findByUsername(submittedData.getUsername());
        if ((possibleExistingUser == null) || (StringUtils.isEmpty(possibleExistingUser.getUsername()))) {
            submittedData.setPassword(bCryptPasswordEncoder.encode(submittedData.getPassword()));

            //Create a json list of roles for a user of this type
            String roles;
            ArrayList<String> roleList = new ArrayList<String>();
            roleList.add("ROLE_USER");
            roleList.add("ROLE_DOCTOR");
            Map<String, Object> rolesJson = new HashMap<String, Object>();
            rolesJson.put("roles", roleList);
            JSONSerializer serializer = new JSONSerializer();
            roles = serializer.include("roles").serialize(rolesJson);

            logger.info("Adding user " + submittedData.getUsername() + " with roles " + roles);
            try {
                // create the User record
                userRepository.save(Arrays.asList(new User(submittedData.getUsername(),
                        submittedData.getPassword(),
                        submittedData.getFname(),
                        submittedData.getLname(),
                        roles,
                        null,
                        null)));

                // create the Doctor record
                doctorRepository.save(new Doctor(submittedData.getUsername(), submittedData.getPracticeName(), submittedData.getPracticeAddress(), submittedData.getRecoveryPhrase()));

                resultString = "SUCCESS";
                logger.info("Created doctor user " + submittedData.getUsername());
            } catch (Exception e) {
                logger.error("Failure creating doctor user " + submittedData.getUsername());
                e.printStackTrace();
            }
        }
        ResultValue result = new ResultValue();
        result.setResult(resultString);
        logger.info("Authenticated user " + currentUser + " completed execution of service /createDoctor");
        return result;
    }

    @Secured({"ROLE_ASSIGN_PERMISSIONS"})
    @RequestMapping(value = "/editPerm", method = RequestMethod.POST)
    public ResultValue editPerm(@RequestBody @Valid CustomPermissionsModel permissionsLists) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Authenticated user " + currentUser + " is starting execution of service /editPerm");
        String resultString = "FAILURE";
        User user = userRepository.findByUsername(permissionsLists.getUsername());

        if ((user != null) && (StringUtils.isNotEmpty(user.getUsername()))) {
            // parse list of permissions that should be added to role based selection of permissions for this user
            List<String> userSuppliedIncludeList = permissionsLists.getCustomadds();
            ArrayList<String> includeList = new ArrayList<String>();
            for (String perm : userSuppliedIncludeList) {
                Permissions permsObject = permissionListRepository.findByPermission(perm);
                if ((permsObject != null) && (StringUtils.isNotEmpty(permsObject.getPermission()))) {
                    logger.info("Permission " + permsObject.getPermission() + " was requested to be added to " + user.getUsername());
                    includeList.add(permsObject.getPermission());
                }
            }

            // parse list of permissions that should be removed from role based selection of permissions for this user
            List<String> userSuppliedExcludeList = permissionsLists.getCustomremoves();
            ArrayList<String> excludeList = new ArrayList<String>();
            for (String perm : userSuppliedExcludeList) {
                Permissions permsObject = permissionListRepository.findByPermission(perm);
                if ((permsObject != null) && (StringUtils.isNotEmpty(permsObject.getPermission()))) {
                    logger.info("Permission " + permsObject.getPermission() + " was requested to be removed from " + user.getUsername());
                    excludeList.add(permsObject.getPermission());
                }
            }

            // update user object

            if (includeList.size() > 0) {
                Map<String, Object> irolesJson = new HashMap<String, Object>();
                irolesJson.put("roles", includeList);
                JSONSerializer serializer = new JSONSerializer();
                String includeRolesJson = serializer.include("roles").serialize(irolesJson);
                user.setCustom_permissions_to_add(includeRolesJson);
            }
            if (excludeList.size() > 0) {
                Map<String, Object> xrolesJson = new HashMap<String, Object>();
                xrolesJson.put("roles", excludeList);
                JSONSerializer xserializer = new JSONSerializer();
                String excludeRolesJson = xserializer.include("roles").serialize(xrolesJson);
                user.setCustom_permissions_to_remove(excludeRolesJson);
            }

            userRepository.save(user);

            if ((includeList.size() > 0) || (excludeList.size() > 0)) resultString = "SUCCESS";
        }
        ResultValue result = new ResultValue();
        result.setResult(resultString);
        logger.info("Authenticated user " + currentUser + " completed execution of service /editPerm");
        return result;
    }


}
