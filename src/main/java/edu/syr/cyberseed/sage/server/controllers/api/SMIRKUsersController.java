package edu.syr.cyberseed.sage.server.controllers.api;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.syr.cyberseed.sage.server.entities.*;
import edu.syr.cyberseed.sage.server.entities.models.*;
import edu.syr.cyberseed.sage.server.repositories.*;
import flexjson.JSONSerializer;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    NurseRepository nurseRepository;
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
    /*
    //creating nurse
    // 5.3 /CreateNurse
    @Secured({"ROLE_ADD_NURSE"})
    @ApiOperation(value = "Add a nurse user profile to the system.",
            notes = "When createNurse service is successfully exercised, the result SHALL be a new Nurse User Profile with default permissions and data for all fields in the Nurse User Profile type with valid non-null values added to the database.  The createNurse service SHALL only be accessible to a user with Add Nurse permission.")
    @RequestMapping(value = "/createNurse", method = RequestMethod.POST)
    public ResultValue createNurse(@RequestBody @Valid NurseUserModel submittedData) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Authenticated user " + currentUser + " is starting execution of service /createNurse");
        String resultString = "FAILURE";


        User possibleExistingUser = userRepository.findByUsername(submittedData.getUsername());
        if ((possibleExistingUser == null) || (StringUtils.isEmpty(possibleExistingUser.getUsername()))) {
            submittedData.setPassword(bCryptPasswordEncoder.encode(submittedData.getPassword()));

            //Create a json list of roles for a user of this type
            String roles;
            ArrayList<String> roleList = new ArrayList<String>();
            roleList.add("ROLE_USER");
            roleList.add("ROLE_NURSE");
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

                // create the Nurse record
                nurseRepository.save(new Nurse(submittedData.getUsername(), submittedData.getPracticeName(), submittedData.getPracticeAddress(), submittedData.getAssociatedDoctors()));

                resultString = "SUCCESS";
                logger.info("Created Nurse user " + submittedData.getUsername());
            } catch (Exception e) {
                logger.error("Failure creating Nurse user " + submittedData.getUsername());
                e.printStackTrace();
            }
        }
        ResultValue result = new ResultValue();
        result.setResult(resultString);
        logger.info("Authenticated user " + currentUser + " completed execution of service /createNurse");
        return result;
    }

    //ending create nurse
*/
    //creating SysAdmins
    // 5.3 /CreateSysAdmin
    @Secured({"ROLE_ADD_SYSTEM_ADMIN"})
    @ApiOperation(value = "Add a system admin user profile to the system.",
            notes = "When createSysAdmin service is successfully exercised, the result SHALL be a new system admin User Profile with default permissions and data for all fields in the system admin User Profile type with valid non-null values added to the database.  The createSysAdmin service SHALL only be accessible to a user with Add System Admin permission.")
    @RequestMapping(value = "/createSysAdmin", method = RequestMethod.POST)
    public ResultValue createSysAdmin(@RequestBody @Valid SystemAdminModel submittedData) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Authenticated user " + currentUser + " is starting execution of service /createSysAdmin");
        String resultString = "FAILURE";


        User possibleExistingUser = userRepository.findByUsername(submittedData.getUsername());
        if ((possibleExistingUser == null) || (StringUtils.isEmpty(possibleExistingUser.getUsername()))) {
            submittedData.setPassword(bCryptPasswordEncoder.encode(submittedData.getPassword()));

            //Create a json list of roles for a user of this type
            String roles;
            ArrayList<String> roleList = new ArrayList<String>();
            roleList.add("ROLE_USER");
            roleList.add("ROLE_SYSTEM_ADMIN");
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

                resultString = "SUCCESS";
                logger.info("Created System Admin user " + submittedData.getUsername());
            } catch (Exception e) {
                logger.error("Failure creating System Admin user " + submittedData.getUsername());
                e.printStackTrace();
            }
        }
        ResultValue result = new ResultValue();
        result.setResult(resultString);
        logger.info("Authenticated user " + currentUser + " completed execution of service /createSysAdmin");
        return result;
    }

    //ending create nurse


    @Secured({"ROLE_ASSIGN_PERMISSIONS"})
    @ApiOperation(value = "Change the permissions of a user profile",
            notes = "When editPerm is successfully exercised, the result SHALL be a change to the permissions field of an existing user profile in the database.  The editPerm service SHALL only be accessible to a user with Assign Permissions permission. \n")

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

    // 5.25 /viewRecoveryPhrase
    @Secured({"ROLE_SYSTEM_ADMIN"})
    @ApiOperation(value = "View a doctor’s recovery phrase.",
            notes = "When viewRecoveryPhrase service is successfully exercised the server application SHALL return the doctor user profile corresponding to the doctor username requested in the service call. The viewRecoveryPhrase service SHALL only return the profile if the accessing has System Administrator Role. The viewRecoveryPhrase service SHALL only return doctor user profiles types.  \n")
    @RequestMapping(value = "/viewRecoveryPhrase/{submittedUsername}", method = RequestMethod.GET)
    public Doctor viewRecoveryPhrase(@PathVariable String submittedUsername) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Authenticated user " + currentUser + " is starting execution of service /viewRecoveryPhrase");

        Doctor resultRecord = null;
        Doctor record = doctorRepository.findByUsername(submittedUsername);
        if ((record != null) && (record.getUsername().equals(submittedUsername))) {
            logger.info("Retrieved doctor record for " + submittedUsername);
            resultRecord = record;
        }
        else {
            logger.warn("Did not find doctor record for " + submittedUsername);
        }

        logger.info("Authenticated user " + currentUser + " completed execution of service /viewRecoveryPhrase");
        // per spec this returns the doctor record from which the recovery phrase may be parsed
        return resultRecord;
    }

}
