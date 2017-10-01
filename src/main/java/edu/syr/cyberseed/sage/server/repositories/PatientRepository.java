package edu.syr.cyberseed.sage.server.repositories;

import edu.syr.cyberseed.sage.server.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
