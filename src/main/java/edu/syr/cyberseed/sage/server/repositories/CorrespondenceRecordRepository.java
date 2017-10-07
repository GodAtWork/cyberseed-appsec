package edu.syr.cyberseed.sage.server.repositories;

import edu.syr.cyberseed.sage.server.entities.CorrespondenceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CorrespondenceRecordRepository extends JpaRepository<CorrespondenceRecord, Long> {
    CorrespondenceRecord findByNoteId(Integer noteId);
    CorrespondenceRecord[] findCorrespondenceRecordsById(Integer id);
}
