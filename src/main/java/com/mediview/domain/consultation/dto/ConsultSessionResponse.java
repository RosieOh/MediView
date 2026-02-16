package com.mediview.domain.consultation.dto;

import com.mediview.domain.enums.ConsultChannel;
import com.mediview.domain.enums.ConsultStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ConsultSessionResponse {

    private Long id;
    private Long appointmentId;
    private ConsultChannel channel;
    private ConsultStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private String webrtcRoomId;
}
