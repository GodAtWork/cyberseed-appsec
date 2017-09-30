package edu.syr.cyberseed.sage.server.repositories;


import edu.syr.cyberseed.sage.server.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
