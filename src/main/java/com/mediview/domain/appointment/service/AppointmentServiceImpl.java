package com.mediview.domain.appointment.service;

import com.mediview.domain.appointment.dto.AppointmentCreateRequest;
import com.mediview.domain.appointment.dto.AppointmentResponse;
import com.mediview.domain.appointment.entity.Appointment;
import com.mediview.domain.appointment.repository.AppointmentRepository;
import com.mediview.domain.enums.AppointmentStatus;
import com.mediview.domain.enums.AppointmentType;
import com.mediview.domain.exception.NotFoundException;
import com.mediview.domain.organization.entity.Organization;
import com.mediview.domain.organization.repository.OrganizationRepository;
import com.mediview.domain.user.entity.User;
import com.mediview.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;

    @Override
    @Transactional
    public AppointmentResponse createAppointment(String patientEmail, AppointmentCreateRequest request) {
        User patient = userRepository.findByEmail(patientEmail)
                .orElseThrow(() -> new NotFoundException("Patient not found"));

        User doctor = userRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new NotFoundException("Doctor not found"));

        Organization org = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new NotFoundException("Organization not found"));

        Integer queueOrder = null;
        if (request.getType() == AppointmentType.QUEUE) {
            List<Appointment> waiting = appointmentRepository.findByDoctorIdAndStatus(
                    doctor.getId(), AppointmentStatus.WAITING);
            queueOrder = waiting.size() + 1;
        }

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .organization(org)
                .type(request.getType())
                .status(request.getType() == AppointmentType.QUEUE
                        ? AppointmentStatus.WAITING : AppointmentStatus.SCHEDULED)
                .scheduledAt(request.getScheduledAt())
                .queueOrder(queueOrder)
                .build();
        appointmentRepository.save(appointment);

        return toResponse(appointment);
    }

    @Override
    public List<AppointmentResponse> getAppointments(String email, AppointmentStatus status) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        List<Appointment> appointments;
        if (status != null) {
            appointments = appointmentRepository.findByPatientIdAndStatus(user.getId(), status);
        } else {
            appointments = appointmentRepository.findByPatientId(user.getId());
        }

        return appointments.stream().map(this::toResponse).toList();
    }

    @Override
    public AppointmentResponse getAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Appointment not found: " + id));
        return toResponse(appointment);
    }

    private AppointmentResponse toResponse(Appointment a) {
        return AppointmentResponse.builder()
                .id(a.getId())
                .patientId(a.getPatient().getId())
                .doctorId(a.getDoctor().getId())
                .organizationId(a.getOrganization().getId())
                .type(a.getType())
                .status(a.getStatus())
                .scheduledAt(a.getScheduledAt())
                .queueOrder(a.getQueueOrder())
                .createdAt(a.getCreatedAt())
                .build();
    }
}
