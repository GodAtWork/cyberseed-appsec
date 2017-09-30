package edu.syr.cyberseed.sage.server.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import edu.syr.cyberseed.sage.server.entities.Record;


public interface RecordRepository extends CrudRepository<Record, Long>{

    List<Record> findByOwner(String owner);
    List<Record> findByPatient(String patient);

}
