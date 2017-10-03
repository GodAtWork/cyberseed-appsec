package edu.syr.cyberseed.sage.server.repositories;

import edu.syr.cyberseed.sage.server.entities.Doctor;
import edu.syr.cyberseed.sage.server.entities.DoctorExamRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorExamRecordRepository extends JpaRepository<DoctorExamRecord, Long> {
    DoctorExamRecord findByDoctor(String doctor);
    DoctorExamRecord findById(Integer record_id);
}
