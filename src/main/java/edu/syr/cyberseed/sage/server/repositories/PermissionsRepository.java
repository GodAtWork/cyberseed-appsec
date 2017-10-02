package edu.syr.cyberseed.sage.server.repositories;

import edu.syr.cyberseed.sage.server.entities.Permissions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionsRepository extends JpaRepository<Permissions, Long> {
    Permissions findByPermission(String permission);
}
