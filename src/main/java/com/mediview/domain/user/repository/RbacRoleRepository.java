package com.mediview.domain.user.repository;

import com.mediview.domain.user.entity.RbacRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RbacRoleRepository extends JpaRepository<RbacRole, Long> {

    Optional<RbacRole> findByName(String name);
}
