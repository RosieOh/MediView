package com.mediview.domain.appointment.service;

import com.mediview.domain.appointment.dto.AppointmentCreateRequest;
import com.mediview.domain.appointment.dto.AppointmentResponse;
import com.mediview.domain.enums.AppointmentStatus;

import java.util.List;

public interface AppointmentService {

    AppointmentResponse createAppointment(String patientEmail, AppointmentCreateRequest request);

    List<AppointmentResponse> getAppointments(String email, AppointmentStatus status);

    AppointmentResponse getAppointment(Long id);
}
