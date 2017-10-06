package edu.syr.cyberseed.sage.server.repositories;

import edu.syr.cyberseed.sage.server.entities.TestResultsRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestResultRecordRepository extends JpaRepository<TestResultsRecord, Long> {
    TestResultsRecord findByDoctor(String doctor);
    TestResultsRecord findById(Integer record_id);
}
