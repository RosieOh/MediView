package com.mediview.domain.consultation.dto;

import com.mediview.domain.enums.ConsultChannel;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConsultSessionCreateRequest {

    @NotNull
    private Long appointmentId;

    @NotNull
    private ConsultChannel channel;
}
