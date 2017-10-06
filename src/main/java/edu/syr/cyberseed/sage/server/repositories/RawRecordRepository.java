package edu.syr.cyberseed.sage.server.repositories;

import edu.syr.cyberseed.sage.server.entities.CorrespondenceRecord;
import edu.syr.cyberseed.sage.server.entities.RawRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RawRecordRepository extends JpaRepository<RawRecord, Long> {
    RawRecord findById(Integer record_id);
}
