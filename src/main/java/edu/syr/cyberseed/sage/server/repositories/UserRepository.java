package edu.syr.cyberseed.sage.server.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.syr.cyberseed.sage.server.entities.User;


@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByName(String name);
}