package edu.syr.cyberseed.sage.server.services;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.syr.cyberseed.sage.server.entities.User;
import edu.syr.cyberseed.sage.server.entities.UserPermissions;
import edu.syr.cyberseed.sage.server.repositories.UserPermissionsRepository;
import edu.syr.cyberseed.sage.server.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPermissionsRepository permissionsRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        // from the user record parse the json list of roles as a list
        // of strings and add each as a spring authority
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonRoleNames = user.getRoles();
            Map<String, Object> mapObject = mapper.readValue(jsonRoleNames,
                    new TypeReference<Map<String, Object>>() {
                    });
            List<String> roleList = (List<String>) mapObject.get("roles");
            for (String roleName : roleList) {
               logger.debug("Found role " + roleName + " for " + user.getUsername());
                grantedAuthorities.add(new SimpleGrantedAuthority(roleName));
            }
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*for (UserPermissions role : user.getRoles()){
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }*/

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
