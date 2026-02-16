package com.mediview.domain.consultation.service;

import com.mediview.domain.appointment.entity.Appointment;
import com.mediview.domain.appointment.repository.AppointmentRepository;
import com.mediview.domain.consultation.dto.ConsultSessionCreateRequest;
import com.mediview.domain.consultation.dto.ConsultSessionResponse;
import com.mediview.domain.consultation.entity.ConsultSession;
import com.mediview.domain.consultation.repository.ConsultSessionRepository;
import com.mediview.domain.enums.AppointmentStatus;
import com.mediview.domain.enums.ConsultStatus;
import com.mediview.domain.exception.BadRequestException;
import com.mediview.domain.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConsultSessionServiceImpl implements ConsultSessionService {

    private final ConsultSessionRepository consultSessionRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional
    public ConsultSessionResponse createSession(ConsultSessionCreateRequest request) {
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new NotFoundException("Appointment not found"));

        if (consultSessionRepository.findByAppointmentId(appointment.getId()).isPresent()) {
            throw new BadRequestException("Session already exists for this appointment");
        }

        appointment.setStatus(AppointmentStatus.IN_PROGRESS);
        appointmentRepository.save(appointment);

        ConsultSession session = ConsultSession.builder()
                .appointment(appointment)
                .channel(request.getChannel())
                .status(ConsultStatus.IN_PROGRESS)
                .startedAt(LocalDateTime.now())
                .webrtcRoomId(UUID.randomUUID().toString())
                .build();
        consultSessionRepository.save(session);

        return toResponse(session);
    }

    @Override
    public ConsultSessionResponse getSession(Long id) {
        ConsultSession session = consultSessionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Session not found: " + id));
        return toResponse(session);
    }

    @Override
    @Transactional
    public ConsultSessionResponse endSession(Long id) {
        ConsultSession session = consultSessionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Session not found: " + id));

        if (session.getStatus() == ConsultStatus.COMPLETED) {
            throw new BadRequestException("Session already completed");
        }

        session.setStatus(ConsultStatus.COMPLETED);
        session.setEndedAt(LocalDateTime.now());
        consultSessionRepository.save(session);

        Appointment appointment = session.getAppointment();
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);

        return toResponse(session);
    }

    private ConsultSessionResponse toResponse(ConsultSession s) {
        return ConsultSessionResponse.builder()
                .id(s.getId())
                .appointmentId(s.getAppointment().getId())
                .channel(s.getChannel())
                .status(s.getStatus())
                .startedAt(s.getStartedAt())
                .endedAt(s.getEndedAt())
                .webrtcRoomId(s.getWebrtcRoomId())
                .build();
    }
}
