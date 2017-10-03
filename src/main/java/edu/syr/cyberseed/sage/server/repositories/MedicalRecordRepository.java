package edu.syr.cyberseed.sage.server.repositories;

import java.util.List;

import edu.syr.cyberseed.sage.server.entities.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long>{

    List<MedicalRecord> findByOwner(String owner);
    List<MedicalRecord> findByPatient(String patient);
    MedicalRecord findById(Integer id);

}
