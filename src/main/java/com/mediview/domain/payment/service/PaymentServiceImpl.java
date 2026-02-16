package com.mediview.domain.payment.service;

import com.mediview.domain.appointment.entity.Appointment;
import com.mediview.domain.appointment.repository.AppointmentRepository;
import com.mediview.domain.enums.PaymentStatus;
import com.mediview.domain.exception.BadRequestException;
import com.mediview.domain.exception.NotFoundException;
import com.mediview.domain.payment.dto.*;
import com.mediview.domain.payment.entity.Payment;
import com.mediview.domain.payment.entity.Refund;
import com.mediview.domain.payment.repository.PaymentRepository;
import com.mediview.domain.payment.repository.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final RefundRepository refundRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional
    public PaymentResponse preparePayment(PaymentPrepareRequest request) {
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new NotFoundException("Appointment not found"));

        Payment payment = Payment.builder()
                .appointment(appointment)
                .amount(request.getAmount())
                .method(request.getMethod())
                .status(PaymentStatus.PENDING)
                .build();
        paymentRepository.save(payment);

        return toResponse(payment);
    }

    @Override
    @Transactional
    public PaymentResponse confirmPayment(PaymentConfirmRequest request) {
        Payment payment = paymentRepository.findById(request.getPaymentId())
                .orElseThrow(() -> new NotFoundException("Payment not found"));

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new BadRequestException("Payment is not in PENDING status");
        }

        payment.setStatus(PaymentStatus.PAID);
        payment.setPgTxId(request.getPgTxId());
        payment.setPaidAt(LocalDateTime.now());
        paymentRepository.save(payment);

        return toResponse(payment);
    }

    @Override
    @Transactional
    public RefundResponse createRefund(RefundRequest request) {
        Payment payment = paymentRepository.findById(request.getPaymentId())
                .orElseThrow(() -> new NotFoundException("Payment not found"));

        if (payment.getStatus() != PaymentStatus.PAID) {
            throw new BadRequestException("Payment is not in PAID status");
        }

        Refund refund = Refund.builder()
                .payment(payment)
                .amount(request.getAmount())
                .reason(request.getReason())
                .status(PaymentStatus.REFUNDED)
                .processedAt(LocalDateTime.now())
                .build();
        refundRepository.save(refund);

        payment.setStatus(PaymentStatus.REFUNDED);
        paymentRepository.save(payment);

        return RefundResponse.builder()
                .id(refund.getId())
                .paymentId(refund.getPayment().getId())
                .amount(refund.getAmount())
                .reason(refund.getReason())
                .status(refund.getStatus())
                .processedAt(refund.getProcessedAt())
                .build();
    }

    private PaymentResponse toResponse(Payment p) {
        return PaymentResponse.builder()
                .id(p.getId())
                .appointmentId(p.getAppointment().getId())
                .amount(p.getAmount())
                .status(p.getStatus())
                .method(p.getMethod())
                .pgTxId(p.getPgTxId())
                .paidAt(p.getPaidAt())
                .build();
    }
}
