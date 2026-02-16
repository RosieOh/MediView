package com.mediview.domain.payment.repository;

import com.mediview.domain.enums.PaymentStatus;
import com.mediview.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByAppointmentId(Long appointmentId);

    List<Payment> findByStatus(PaymentStatus status);

    Optional<Payment> findByPgTxId(String pgTxId);
}
