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
    MedicalAdminRepository medicalAdminRepository;
    @Autowired
    InsuranceAdminRepository insAdminRepository;
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
                userRepository.save(new User(submittedData.getUsername(),
                        submittedData.getPassword(),
                        submittedData.getFname(),
                        submittedData.getLname(),
                        roles,
                        null,
                        null));
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

    //creating nurse

    // 5.3 /CreateNurse
    @Secured({"ROLE_ADD_NURSE"})
    @ApiOperation(value = "Add a nurse user profile to the system.",
            notes = "When createNurse service is successfully exercised, the result SHALL be a new Nurse User Profile with default permissions and data for all fields in the nurse User Profile type with valid non-null values added to the database.  The createNurse service SHALL only be accessible to a user with Add nurse permission.")
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
                nurseRepository.save(Arrays.asList(new Nurse(submittedData.getUsername(), submittedData.getPname(), submittedData.getPaddress(), submittedData.getAdoctors())));

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

    // 5.4 /CreateSysAdmin
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

    //creating Medical Admin

    // 5.5 /CreateMedAdmin
    @Secured({"ROLE_ADD_MEDICAL_ADMIN"})
    @ApiOperation(value = "Add a medical admin user profile to the system.",
            notes = "When createMedAdmin service is successfully exercised, the result SHALL be a new medical admin User Profile with default permissions and data for all fields in the medical admin User Profile type with valid non-null values added to the database.  The medical admin service SHALL only be accessible to a user with Add medical admin permission.")
    @RequestMapping(value = "/createMedAdmin", method = RequestMethod.POST)
    public ResultValue createMedAdmin(@RequestBody @Valid MedicalAdminModel submittedData) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Authenticated user " + currentUser + " is starting execution of service /createMedAdmin");
        String resultString = "FAILURE";


        User possibleExistingUser = userRepository.findByUsername(submittedData.getUsername());
        if ((possibleExistingUser == null) || (StringUtils.isEmpty(possibleExistingUser.getUsername()))) {
            submittedData.setPassword(bCryptPasswordEncoder.encode(submittedData.getPassword()));

            //Create a json list of roles for a user of this type
            String roles;
            ArrayList<String> roleList = new ArrayList<String>();
            roleList.add("ROLE_USER");
            roleList.add("ROLE_MEDICAL_ADMIN");
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
                medicalAdminRepository.save(Arrays.asList(new Medical_admin(submittedData.getUsername(), submittedData.getPname(), submittedData.getPaddress(), submittedData.getAdoctor(), submittedData.getAnurse())));

                resultString = "SUCCESS";
                logger.info("Created Medical Admin user " + submittedData.getUsername());
            } catch (Exception e) {
                logger.error("Failure creating Medical Admin user " + submittedData.getUsername());
                e.printStackTrace();
            }
        }
        ResultValue result = new ResultValue();
        result.setResult(resultString);
        logger.info("Authenticated user " + currentUser + " completed execution of service /createMedAdmin");
        return result;
    }

    //ending create Medical Admin

    //creating ins admin

    // 5.6 /CreateInsAdmin
    @Secured({"ROLE_ADD_INSURANCE_ADMIN"})
    @ApiOperation(value = "Add a insurance admin user profile to the system.",
            notes = "When createInsAdmin service is successfully exercised, the result SHALL be a new insurance admin User Profile with default permissions and data for all fields in the insurance admin User Profile type with valid non-null values added to the database.  The createInsAdmin service SHALL only be accessible to a user with Add insurance admin permission.")
    @RequestMapping(value = "/createInsAdmin", method = RequestMethod.POST)
    public ResultValue createInsAdmin(@RequestBody @Valid InsuranceAdminModel submittedData) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Authenticated user " + currentUser + " is starting execution of service /createInsAdmin");
        String resultString = "FAILURE";


        User possibleExistingUser = userRepository.findByUsername(submittedData.getUsername());
        if ((possibleExistingUser == null) || (StringUtils.isEmpty(possibleExistingUser.getUsername()))) {
            submittedData.setPassword(bCryptPasswordEncoder.encode(submittedData.getPassword()));

            //Create a json list of roles for a user of this type
            String roles;
            ArrayList<String> roleList = new ArrayList<String>();
            roleList.add("ROLE_USER");
            roleList.add("ROLE_INSURANCE_ADMIN");
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

                // create the InsAdmin record
                insAdminRepository.save(Arrays.asList(new Insurance_admin(submittedData.getUsername(), submittedData.getCname(), submittedData.getCaddress())));

                resultString = "SUCCESS";
                logger.info("Created Insurance Admin user " + submittedData.getUsername());
            } catch (Exception e) {
                logger.error("Failure creating Inurance Admin user " + submittedData.getUsername());
                e.printStackTrace();
            }
        }
        ResultValue result = new ResultValue();
        result.setResult(resultString);
        logger.info("Authenticated user " + currentUser + " completed execution of service /createInsAdmin");
        return result;
    }

    //ending create ins admin

    //5.7 /editPerm
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

    // 5.18 /editPatient
    @Secured({"ROLE_EDIT_PATIENT"})
    @ApiOperation(value = "Edit existing patient user profile in the system.",
            notes = "The editPatient service SHALL provide the capability to changing any of the fields in a patient user profile except the Permissions and Roles list. When editPatient service is successfully exercised, the result SHALL be one or more changed value(s) in a patient user profile fields.  The editPatient service SHALL only update a patient user profile if the calling user has Edit Patient permissions.")
    @RequestMapping(value = "/editPatient", method = RequestMethod.POST)
    public ResultValue editPatient(@RequestBody @Valid EditPatientUserModel submittedData) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Authenticated user " + currentUser + " is starting execution of service /editPatient");
        String resultString = "FAILURE";
        User user = userRepository.findByUsername(submittedData.getUsername());
        Patient patient = patientRepository.findByUsername(submittedData.getUsername());
        Boolean userExists = ((user != null) && (StringUtils.isNotEmpty(user.getUsername()))) ? true : false;
        Boolean patientExists = ((patient != null) && (StringUtils.isNotEmpty(patient.getUsername()))) ? true : false;

        if (userExists && patientExists) {

            // set values for user record
            if (submittedData.getFname() != null) {
                user.setFname(submittedData.getFname());
            }
            if (submittedData.getLname() != null) {
                user.setLname(submittedData.getLname());
            }
            if (submittedData.getPassword() != null) {
                user.setPassword(bCryptPasswordEncoder.encode(submittedData.getPassword()));
            }

            // set values for patient record
            if (submittedData.getAddress() != null) {
                patient.setAddress(submittedData.getAddress());
            }
            if (submittedData.getDob() != null ) {
                patient.setDob(submittedData.getDob());
            }
            if (submittedData.getSsn() != null) {
                patient.setSsn(submittedData.getSsn());
            }

            User savedUser = userRepository.save(user);
            Patient savedPatient = patientRepository.save(patient);

            if ((savedUser != null) && (savedPatient != null)) {
                resultString = "SUCCESS";
                logger.info(currentUser + " completed editing patient " + submittedData.getUsername());
            }
        }
        else {
            logger.warn(currentUser + " tried to edit patient " + submittedData.getUsername() + " but there is not a complete patient record to edit");
        }
        ResultValue result = new ResultValue();
        result.setResult(resultString);
        logger.info("Authenticated user " + currentUser + " completed execution of service /editPatient");
        return result;
    }

    // 5.19 /editDoctor
    @Secured({"ROLE_EDIT_DOCTOR"})
    @ApiOperation(value = "Edit existing doctor user profile in the system.",
            notes = "The editDoctor service SHALL provide the capability to changing any of the fields in a doctor user profile except the Permissions and Roles list.  When editDoctor service is successfully exercised, the result SHALL be one or more changed value(s) in a doctor user profile fields.  The editDoctor service SHALL only update a doctor user profile if the calling user has Edit Doctor permissions.")
    @RequestMapping(value = "/editDoctor", method = RequestMethod.POST)
    public ResultValue editDoctor(@RequestBody @Valid EditDoctorUserModel submittedData) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Authenticated user " + currentUser + " is starting execution of service /editDoctor");
        String resultString = "FAILURE";
        User user = userRepository.findByUsername(submittedData.getUsername());
        Doctor doctor = doctorRepository.findByUsername(submittedData.getUsername());
        Boolean userExists = ((user != null) && (StringUtils.isNotEmpty(user.getUsername()))) ? true : false;
        Boolean doctorExists = ((doctor != null) && (StringUtils.isNotEmpty(doctor.getUsername()))) ? true : false;

        if (userExists && doctorExists) {

            // set values for user record
            if (submittedData.getFname() != null) {
                user.setFname(submittedData.getFname());
            }
            if (submittedData.getLname() != null) {
                user.setLname(submittedData.getLname());
            }
            if (submittedData.getPassword() != null) {
                user.setPassword(bCryptPasswordEncoder.encode(submittedData.getPassword()));
            }

            // set values for doctor record
            if (submittedData.getPracticeAddress() != null) {
                doctor.setPaddress(submittedData.getPracticeAddress());
            }
            if (submittedData.getPracticeName() != null ) {
                doctor.setPname(submittedData.getPracticeName());
            }
            if (submittedData.getRecoveryPhrase() != null) {
                doctor.setRphrase(submittedData.getRecoveryPhrase());
            }

            User savedUser = userRepository.save(user);
            Doctor savedDoctor = doctorRepository.save(doctor);

            if ((savedUser != null) && (savedDoctor != null)) {
                resultString = "SUCCESS";
                logger.info(currentUser + " completed editing doctor " + submittedData.getUsername());
            }
        }
        else {
            logger.warn(currentUser + " tried to edit doctor " + submittedData.getUsername() + " but there is not a complete doctor record to edit");
        }
        ResultValue result = new ResultValue();
        result.setResult(resultString);
        logger.info("Authenticated user " + currentUser + " completed execution of service /editDoctor");
        return result;
    }

    //starting edit nurse
    // 5.20 /editNurse
    @Secured({"ROLE_EDIT_NURSE"})
    @ApiOperation(value = "Edit existing nurse user profile in the system.",
            notes = "The editNurse service SHALL provide the capability to changing any of the fields in a nurse user profile except the Permissions and Roles list.  When editNurse service is successfully exercised, the result SHALL be one or more changed value(s) in a Nurse user profile fields.  The editNurse service SHALL only update a nurse user profile if the calling user has Edit nurse permissions.")
    @RequestMapping(value = "/editNurse", method = RequestMethod.POST)
    public ResultValue editNurse(@RequestBody @Valid EditNurseUserModel submittedData) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Authenticated user " + currentUser + " is starting execution of service /editNurse");
        String resultString = "FAILURE";
        User user = userRepository.findByUsername(submittedData.getUsername());
        Nurse nurse = nurseRepository.findByUsername(submittedData.getUsername());
        Boolean userExists = ((user != null) && (StringUtils.isNotEmpty(user.getUsername()))) ? true : false;
        Boolean nurseExists = ((nurse != null) && (StringUtils.isNotEmpty(nurse.getUsername()))) ? true : false;

        if (userExists && nurseExists) {

            // set values for user record
            if (submittedData.getFname() != null) {
                user.setFname(submittedData.getFname());
            }
            if (submittedData.getLname() != null) {
                user.setLname(submittedData.getLname());
            }
            if (submittedData.getPassword() != null) {
                user.setPassword(bCryptPasswordEncoder.encode(submittedData.getPassword()));
            }

            // set values for doctor record
            if (submittedData.getPaddress() != null) {
                nurse.setPaddress(submittedData.getPaddress());
            }
            if (submittedData.getPname() != null ) {
                nurse.setPname(submittedData.getPname());
            }
            if (submittedData.getAdoctors() != null) {
                nurse.setAdoctors(submittedData.getAdoctors());
            }

            User savedUser = userRepository.save(user);
            Nurse savedNurse = nurseRepository.save(nurse);

            if ((savedUser != null) && (savedNurse != null)) {
                resultString = "SUCCESS";
                logger.info(currentUser + " completed editing nurse " + submittedData.getUsername());
            }
        }
        else {
            logger.warn(currentUser + " tried to edit nurse " + submittedData.getUsername() + " but there is not a complete nurse record to edit");
        }
        ResultValue result = new ResultValue();
        result.setResult(resultString);
        logger.info("Authenticated user " + currentUser + " completed execution of service /editNurse");
        return result;
    }
    //ending edit nurse


    //starting edit sysAdmin
    // 5.21 /editsysAdmin
    @Secured({"ROLE_EDIT_SYSTEM_ADMIN"})
    @ApiOperation(value = "Edit existing system admin user profile in the system.",
            notes = "The editSysAdmin service SHALL provide the capability to changing any of the fields in a system admin user profile except the Permissions and Roles list.  When editSysAdmin service is successfully exercised, the result SHALL be one or more changed value(s) in a System Admin user profile fields.  The editSysAdmin service SHALL only update a system admin user profile if the calling user has Edit system admin permissions.")
    @RequestMapping(value = "/editSysAdmin", method = RequestMethod.POST)
    public ResultValue editSysAdmin(@RequestBody @Valid EditSysAdminUserModel submittedData) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Authenticated user " + currentUser + " is starting execution of service /editSysAdmin");
        String resultString = "FAILURE";
        User user = userRepository.findByUsername(submittedData.getUsername());
        Boolean userExists = ((user != null) && (StringUtils.isNotEmpty(user.getUsername()))) ? true : false;

        if (userExists) {

            // set values for user record
            if (submittedData.getFname() != null) {
                user.setFname(submittedData.getFname());
            }
            if (submittedData.getLname() != null) {
                user.setLname(submittedData.getLname());
            }
            if (submittedData.getPassword() != null) {
                user.setPassword(bCryptPasswordEncoder.encode(submittedData.getPassword()));
            }

            User savedUser = userRepository.save(user);

            if ((savedUser != null)) {
                resultString = "SUCCESS";
                logger.info(currentUser + " completed editing system admin " + submittedData.getUsername());
            }
        }
        else {
            logger.warn(currentUser + " tried to edit system admin " + submittedData.getUsername() + " but there is not a complete system admin record to edit");
        }
        ResultValue result = new ResultValue();
        result.setResult(resultString);
        logger.info("Authenticated user " + currentUser + " completed execution of service /editSysAdmin");
        return result;
    }
    //ending edit sysAdmin


    //starting edit MedAdmin
    // 5.22 /editMedAdmin
    @Secured({"ROLE_EDIT_MEDICAL_ADMIN"})
    @ApiOperation(value = "Edit existing medical admin user profile in the system.",
            notes = "The editMedAdmin service SHALL provide the capability to changing any of the fields in a medical admin user profile except the Permissions and Roles list.  When editMedAdmin service is successfully exercised, the result SHALL be one or more changed value(s) in a Medical Admin user profile fields.  The editMedAdmin service SHALL only update a medical admin user profile if the calling user has Edit medical admin permissions.")
    @RequestMapping(value = "/editMedAdmin", method = RequestMethod.POST)
    public ResultValue editMedAdmin(@RequestBody @Valid EditMedAdminUserModel submittedData) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Authenticated user " + currentUser + " is starting execution of service /editMedAdmin");
        String resultString = "FAILURE";
        User user = userRepository.findByUsername(submittedData.getUsername());
        Medical_admin medical_admin = medicalAdminRepository.findByUsername(submittedData.getUsername());
        Boolean userExists = ((user != null) && (StringUtils.isNotEmpty(user.getUsername()))) ? true : false;
        Boolean medical_adminExists = ((medical_admin != null) && (StringUtils.isNotEmpty(medical_admin.getUsername()))) ? true : false;

        if (userExists && medical_adminExists) {

            // set values for user record
            if (submittedData.getFname() != null) {
                user.setFname(submittedData.getFname());
            }
            if (submittedData.getLname() != null) {
                user.setLname(submittedData.getLname());
            }
            if (submittedData.getPassword() != null) {
                user.setPassword(bCryptPasswordEncoder.encode(submittedData.getPassword()));
            }

            // set values for doctor record
            if (submittedData.getPaddress() != null) {
                medical_admin.setPaddress(submittedData.getPaddress());
            }
            if (submittedData.getPname() != null ) {
                medical_admin.setPname(submittedData.getPname());
            }
            if (submittedData.getAdoctor() != null) {
                medical_admin.setAdoctor(submittedData.getAdoctor());
            }
            if (submittedData.getAnurse() != null) {
                medical_admin.setAnurse(submittedData.getAnurse());
            }

            User savedUser = userRepository.save(user);
            Medical_admin savedMedicalAdmin = medicalAdminRepository.save(medical_admin);

            if ((savedUser != null) && (savedMedicalAdmin != null)) {
                resultString = "SUCCESS";
                logger.info(currentUser + " completed editing medical admin " + submittedData.getUsername());
            }
        }
        else {
            logger.warn(currentUser + " tried to edit medical admin " + submittedData.getUsername() + " but there is not a complete medical admin record to edit");
        }
        ResultValue result = new ResultValue();
        result.setResult(resultString);
        logger.info("Authenticated user " + currentUser + " completed execution of service /editMedAdmin");
        return result;
    }
    //ending edit MedAdmin


    //starting edit InsAdmin
    // 5.23 /editInsAdmin
    @Secured({"ROLE_EDIT_INSURANCE_ADMIN"})
    @ApiOperation(value = "Edit existing insurance admin user profile in the system.",
            notes = "The editInsAdmin service SHALL provide the capability to changing any of the fields in a insurance admin user profile except the Permissions and Roles list.  When editInsAdmin service is successfully exercised, the result SHALL be one or more changed value(s) in a Insurance Admin user profile fields.  The editInsAdmin service SHALL only update a insurance admin user profile if the calling user has Edit insurance admin permissions.")
    @RequestMapping(value = "/editInsAdmin", method = RequestMethod.POST)
    public ResultValue editInsAdmin(@RequestBody @Valid EditInsAdminUserModel submittedData) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Authenticated user " + currentUser + " is starting execution of service /editInsAdmin");
        String resultString = "FAILURE";
        User user = userRepository.findByUsername(submittedData.getUsername());
        Insurance_admin insurance_admin = insAdminRepository.findByUsername(submittedData.getUsername());
        Boolean userExists = ((user != null) && (StringUtils.isNotEmpty(user.getUsername()))) ? true : false;
        Boolean insurance_adminExists = ((insurance_admin != null) && (StringUtils.isNotEmpty(insurance_admin.getUsername()))) ? true : false;

        if (userExists && insurance_adminExists) {

            // set values for user record
            if (submittedData.getFname() != null) {
                user.setFname(submittedData.getFname());
            }
            if (submittedData.getLname() != null) {
                user.setLname(submittedData.getLname());
            }
            if (submittedData.getPassword() != null) {
                user.setPassword(bCryptPasswordEncoder.encode(submittedData.getPassword()));
            }

            // set values for insurance admin record
            if (submittedData.getCname() != null ) {
                insurance_admin.setCname(submittedData.getCname());
            }
            if (submittedData.getCaddress() != null) {
                insurance_admin.setCaddress(submittedData.getCaddress());
            }


            User savedUser = userRepository.save(user);
            Insurance_admin savedInsuranceAdmin = insAdminRepository.save(insurance_admin);

            if ((savedUser != null) && (savedInsuranceAdmin != null)) {
                resultString = "SUCCESS";
                logger.info(currentUser + " completed editing insurance admin " + submittedData.getUsername());
            }
        }
        else {
            logger.warn(currentUser + " tried to edit insurance admin " + submittedData.getUsername() + " but there is not a complete insurance admin record to edit");
        }
        ResultValue result = new ResultValue();
        result.setResult(resultString);
        logger.info("Authenticated user " + currentUser + " completed execution of service /editInsAdmin");
        return result;
    }
    //ending edit InsAdmin

    // 5.24 /viewPatientProfile
    @Secured({"ROLE_VIEW_PII"})
    @ApiOperation(value = "View a patient.",
            notes = "When viewPatientProfile service is successfully exercised the server application SHALL return the patient user profile corresponding to the patient username requested in the service call. The viewPatientProfile service SHALL only return the profile if the accessing user has View PII permission. The viewPatientProfile service SHALL only return patient user profiles types.  \n")
    @RequestMapping(value = "/viewPatientProfile/{submittedUsername}", method = RequestMethod.GET)
    public ArrayList<String> viewPatient(@PathVariable String submittedUsername) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean currentUserisInsuranceAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_INSURANCE_ADMIN"));
        logger.info("Authenticated user " + currentUser + " is starting execution of service /viewPatient");
        logger.info("Authenticated user " + currentUser + " is a Insurance Admin? answer: " + currentUserisInsuranceAdmin);
        boolean currentUserisMedicalAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MEDICAL_ADMIN"));
        logger.info("Authenticated user " + currentUser + " is starting execution of service /viewPatient");
        logger.info("Authenticated user " + currentUser + " is a Medical Admin? answer: " + currentUserisMedicalAdmin);

        Patient possibleExistingPatient = patientRepository.findByUsername(submittedUsername);
        Boolean patientExists = (possibleExistingPatient != null) ? true : false;
        ArrayList<String> recordList = new ArrayList<String>();
        if (patientExists){
            User record = userRepository.findByUsername(submittedUsername);
            Patient precord = patientRepository.findByUsername(submittedUsername);
            // parse the list of viewers stored in the db as a json list to a java arraylist
            recordList.add(record.getUsername());
            recordList.add(record.getFname());
            recordList.add(record.getLname());
            recordList.add(record.getRoles());
            recordList.add(record.getCustom_permissions_to_add());
            recordList.add(precord.getAddress());
            recordList.add(precord.getSsn().toString());
            recordList.add(precord.getDob().toString());

        }else{
            recordList.add("FAILURE");
            logger.info("Given user " + submittedUsername + " is not a patient");
        }
        logger.info("Authenticated user " + currentUser + " completed execution of service /viewPatient");
        return recordList;
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

    // 5.26 /removeUserProfile
    @Secured({"ROLE_DELETE_USER_PROFILE"})
    @ApiOperation(value = "Remove existing user profile in the system.",
            notes = "The removeUserProfile service SHALL provide the capability to remove an existing user profile from the system. The removeUserProfilet service SHALL only remove the user profile specified in the call to the service. The removeUserProfile service SHALL only remove a patient user profile if the calling user has Delete User Profile permissions")
    @RequestMapping(value = "/removeUserProfile", method = RequestMethod.POST)
    public ResultValue removeUserProfile(@RequestBody @Valid RemoveUserModel submittedData) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Authenticated user " + currentUser + " is starting execution of service /editPatient");
        String resultString = "FAILURE";
        User user = userRepository.findByUsername(submittedData.getUsername());
        Boolean userExists = ((user != null) && (StringUtils.isNotEmpty(user.getUsername()))) ? true : false;

        if (userExists ) {
            userRepository.deleteUserByUsername(user.getUsername());
            // todo verify cascade or remove subtypes

            resultString = "SUCCESS";
            logger.info(currentUser + " completed removing user profile for " + submittedData.getUsername());

        }
        else {
            logger.warn(currentUser + " tried to remove user profile for user " + submittedData.getUsername() + " but this user does not exist");
        }
        ResultValue result = new ResultValue();
        result.setResult(resultString);
        logger.info("Authenticated user " + currentUser + " completed execution of service /removeUserProfile");
        return result;
    }

}
