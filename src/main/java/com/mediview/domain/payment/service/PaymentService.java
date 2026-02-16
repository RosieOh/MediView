package com.mediview.domain.payment.service;

import com.mediview.domain.payment.dto.*;

public interface PaymentService {

    PaymentResponse preparePayment(PaymentPrepareRequest request);

    PaymentResponse confirmPayment(PaymentConfirmRequest request);

    RefundResponse createRefund(RefundRequest request);
}
