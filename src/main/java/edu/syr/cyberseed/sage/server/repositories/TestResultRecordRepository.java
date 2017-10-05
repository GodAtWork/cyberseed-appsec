package edu.syr.cyberseed.sage.server.repositories;

import edu.syr.cyberseed.sage.server.entities.Doctor;
import edu.syr.cyberseed.sage.server.entities.Record_testresult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestResultRecordRepository extends JpaRepository<Record_testresult, Long> {
    Record_testresult findByDoctor(String doctor);
    Record_testresult findById(Integer record_id);
}
