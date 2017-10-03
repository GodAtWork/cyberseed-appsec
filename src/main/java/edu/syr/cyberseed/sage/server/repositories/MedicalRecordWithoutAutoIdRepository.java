package edu.syr.cyberseed.sage.server.repositories;

import edu.syr.cyberseed.sage.server.entities.MedicalRecord;
import edu.syr.cyberseed.sage.server.entities.MedicalRecordWithoutAutoId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MedicalRecordWithoutAutoIdRepository extends JpaRepository<MedicalRecordWithoutAutoId, Long>{

    List<MedicalRecordWithoutAutoId> findByOwner(String owner);
    List<MedicalRecordWithoutAutoId> findByPatient(String patient);
    MedicalRecordWithoutAutoId findById(Integer id);

}
