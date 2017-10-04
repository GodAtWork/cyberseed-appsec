package edu.syr.cyberseed.sage.server.repositories;

import edu.syr.cyberseed.sage.server.entities.Insurance_admin;
import edu.syr.cyberseed.sage.server.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsuranceAdminRepository extends JpaRepository<Insurance_admin, Long> {
    Insurance_admin findByUsername(String username);
}
