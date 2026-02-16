package com.mediview.domain.user.entity;

import com.mediview.domain.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_rbac_user_role")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RbacUserRole extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private RbacRole role;
}
