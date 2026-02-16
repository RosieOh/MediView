package com.mediview.domain.intake.entity;

import com.mediview.domain.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_intake_file")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntakeFile extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "intake_form_id", nullable = false)
    private IntakeForm intakeForm;

    @Column(nullable = false)
    private String objectKey;

    private String mimeType;

    private Long fileSize;
}
