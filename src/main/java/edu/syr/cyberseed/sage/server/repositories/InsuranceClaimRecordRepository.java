package edu.syr.cyberseed.sage.server.repositories;

import edu.syr.cyberseed.sage.server.entities.InsuranceClaimRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsuranceClaimRecordRepository extends JpaRepository<InsuranceClaimRecord, Long> {
    InsuranceClaimRecord findById(Integer id);
}
