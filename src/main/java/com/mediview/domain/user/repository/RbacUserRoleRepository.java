package com.mediview.domain.user.repository;

import com.mediview.domain.user.entity.RbacUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RbacUserRoleRepository extends JpaRepository<RbacUserRole, Long> {

    List<RbacUserRole> findByUserId(Long userId);

    List<RbacUserRole> findByRoleId(Long roleId);
}
