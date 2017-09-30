package edu.syr.cyberseed.sage.server.services;

import edu.syr.cyberseed.sage.server.entities.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
}
