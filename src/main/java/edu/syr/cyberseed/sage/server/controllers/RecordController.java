package edu.syr.cyberseed.sage.server.controllers;

import java.util.*;

import edu.syr.cyberseed.sage.server.entities.*;
import edu.syr.cyberseed.sage.server.entities.models.CustomPermissionsModel;
import edu.syr.cyberseed.sage.server.entities.models.PatientUserModel;
import edu.syr.cyberseed.sage.server.repositories.PatientRepository;
import edu.syr.cyberseed.sage.server.repositories.PermissionsRepository;
import edu.syr.cyberseed.sage.server.repositories.RecordRepository;
import edu.syr.cyberseed.sage.server.repositories.UserRepository;
import flexjson.JSONSerializer;
import org.apache.commons.lang3.StringUtils;
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
    @Autowired
    PermissionsRepository permissionListRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(RecordController.class);

    @RequestMapping(value = "/createPatient", method = RequestMethod.POST)
     public ResultValue createPatient(@RequestBody @Valid PatientUserModel user) {
        logger.info("Starting execution of service /createPatient");
        String resultString = "FAILURE";
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        //Create a json list of roles for a user of this type
        String roles;
        ArrayList<String> roleList = new ArrayList<String>();
        roleList.add("ROLE_USER");
        roleList.add("ROLE_PATIENT");
        Map<String, Object> rolesJson = new HashMap<String, Object>();
        rolesJson.put("roles", roleList);
        JSONSerializer serializer = new JSONSerializer();
        roles = serializer.include("roles").serialize(rolesJson);

        logger.info("Adding user " +  user.getUsername() + " with roles " + roles);
        try {
            // create the User record
            userRepository.save(Arrays.asList(new User(user.getUsername(),
                    user.getPassword(),
                    user.getFname(),
                    user.getLname(),
                    roles,
                    null,
                    null)));
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
        logger.info("Completed execution of service /createPatient");
        return result;
    }

    @RequestMapping(value = "/editPerm", method = RequestMethod.POST)
    public ResultValue editPerm(@RequestBody @Valid CustomPermissionsModel permissionsLists) {
        logger.info("Starting execution of service /editPerm");
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
        logger.info("Completed execution of service /editPerm");
        return result;
    }


}
