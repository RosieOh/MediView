package com.mediview.domain.policy.entity;

import com.mediview.domain.audit.BaseEntity;
import com.mediview.domain.enums.PolicyCategory;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_policy")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Policy extends BaseEntity {

    @Column(name = "policy_key", nullable = false, unique = true)
    private String key;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PolicyCategory category;

    private Integer version;

    @Column(columnDefinition = "JSON")
    private String configJson;

    private LocalDateTime activeFrom;

    private LocalDateTime activeTo;
}
