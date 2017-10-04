package edu.syr.cyberseed.sage.server.repositories;

import edu.syr.cyberseed.sage.server.entities.Medical_admin;
import edu.syr.cyberseed.sage.server.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalAdminRepository extends JpaRepository<Medical_admin, Long> {
    Medical_admin findByUsername(String username);
}
