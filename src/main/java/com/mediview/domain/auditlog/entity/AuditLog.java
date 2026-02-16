package com.mediview.domain.auditlog.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_audit_log")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long actorId;

    @Column(nullable = false)
    private String action;

    private String targetType;

    private Long targetId;

    private String ip;

    private String userAgent;

    @Column(columnDefinition = "JSON")
    private String metadataJson;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
