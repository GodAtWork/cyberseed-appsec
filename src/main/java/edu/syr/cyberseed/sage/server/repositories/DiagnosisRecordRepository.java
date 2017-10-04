package edu.syr.cyberseed.sage.server.repositories;

import edu.syr.cyberseed.sage.server.entities.DiagnosisRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiagnosisRecordRepository extends JpaRepository<DiagnosisRecord, Long> {
    DiagnosisRecord findByDoctor(String doctor);
    DiagnosisRecord findById(Integer record_id);
}
