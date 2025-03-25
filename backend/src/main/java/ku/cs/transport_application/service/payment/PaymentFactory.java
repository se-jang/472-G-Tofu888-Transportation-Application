package ku.cs.transport_application.service.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PaymentFactory {
    private final Map<String, PaymentService> paymentServices;

    @Autowired
    public PaymentFactory(Map<String, PaymentService> paymentServices) {
        this.paymentServices = paymentServices;
    }

    public PaymentService getPaymentService(String method) {
        return paymentServices.get(method);
    }
}
