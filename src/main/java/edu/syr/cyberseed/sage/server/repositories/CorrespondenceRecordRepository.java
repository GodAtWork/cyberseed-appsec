package edu.syr.cyberseed.sage.server.repositories;

import edu.syr.cyberseed.sage.server.entities.CorrespondenceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CorrespondenceRecordRepository extends JpaRepository<CorrespondenceRecord, Long> {
    CorrespondenceRecord findById(Integer record_id);
}
