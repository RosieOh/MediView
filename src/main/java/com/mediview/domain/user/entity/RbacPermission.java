package com.mediview.domain.user.entity;

import com.mediview.domain.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_rbac_permission")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RbacPermission extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String code;

    private String description;

    private String resource;

    private String action;
}
