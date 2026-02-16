package com.mediview.domain.intake.service;

import com.mediview.domain.intake.dto.IntakeFormRequest;
import com.mediview.domain.intake.dto.IntakeFormResponse;

import java.util.List;

public interface IntakeService {

    IntakeFormResponse createIntakeForm(Long appointmentId, IntakeFormRequest request);

    List<IntakeFormResponse> getIntakeForms(Long appointmentId);
}
