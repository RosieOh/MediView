package com.mediview.domain.policy.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_policy_violation")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyViolation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long actorId;

    @Column(columnDefinition = "TEXT")
    private String context;

    @Column(nullable = false)
    private String ruleKey;

    @Column(nullable = false)
    private String severity;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
