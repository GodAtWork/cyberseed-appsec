package edu.syr.cyberseed.sage.server.services;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.syr.cyberseed.sage.server.entities.User;
import edu.syr.cyberseed.sage.server.repositories.UserRepository;

@Service
public class UsersService implements UserDetailsService {

    private UserRepository repo;

    @Autowired
    public UsersService(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = repo.findByUsername(username);
        if (user == null) {
            return null;
        }
        List<GrantedAuthority> auth = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");

        if (username.equals("admin")) {
            auth = AuthorityUtils
                    .commaSeparatedStringToAuthorityList("ROLE_USER,ROLE_ADMIN");
        }
        else if (username.equals("sampleuser")) {
            auth = AuthorityUtils
                    .commaSeparatedStringToAuthorityList("ROLE_USER");
        }

        String password = user.getPassword();
        return new org.springframework.security.core.userdetails.User(username, password,
                auth);
    }

}