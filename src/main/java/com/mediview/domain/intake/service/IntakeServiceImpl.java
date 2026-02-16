package com.mediview.domain.intake.service;

import com.mediview.domain.appointment.entity.Appointment;
import com.mediview.domain.appointment.repository.AppointmentRepository;
import com.mediview.domain.exception.NotFoundException;
import com.mediview.domain.intake.dto.IntakeFormRequest;
import com.mediview.domain.intake.dto.IntakeFormResponse;
import com.mediview.domain.intake.entity.IntakeForm;
import com.mediview.domain.intake.repository.IntakeFormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IntakeServiceImpl implements IntakeService {

    private final IntakeFormRepository intakeFormRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional
    public IntakeFormResponse createIntakeForm(Long appointmentId, IntakeFormRequest request) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Appointment not found: " + appointmentId));

        IntakeForm form = IntakeForm.builder()
                .appointment(appointment)
                .rawText(request.getRawText())
                .structuredJson(request.getStructuredJson())
                .submittedAt(LocalDateTime.now())
                .build();
        intakeFormRepository.save(form);

        return toResponse(form);
    }

    @Override
    public List<IntakeFormResponse> getIntakeForms(Long appointmentId) {
        return intakeFormRepository.findByAppointmentId(appointmentId).stream()
                .map(this::toResponse)
                .toList();
    }

    private IntakeFormResponse toResponse(IntakeForm form) {
        return IntakeFormResponse.builder()
                .id(form.getId())
                .appointmentId(form.getAppointment().getId())
                .rawText(form.getRawText())
                .structuredJson(form.getStructuredJson())
                .submittedAt(form.getSubmittedAt())
                .build();
    }
}
