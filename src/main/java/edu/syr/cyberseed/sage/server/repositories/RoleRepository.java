package edu.syr.cyberseed.sage.server.repositories;


import edu.syr.cyberseed.sage.server.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
