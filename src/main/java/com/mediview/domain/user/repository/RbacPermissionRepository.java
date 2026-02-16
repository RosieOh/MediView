package com.mediview.domain.user.repository;

import com.mediview.domain.user.entity.RbacPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RbacPermissionRepository extends JpaRepository<RbacPermission, Long> {

    Optional<RbacPermission> findByCode(String code);
}
