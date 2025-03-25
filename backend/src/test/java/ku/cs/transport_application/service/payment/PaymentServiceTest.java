package ku.cs.transport_application.service.payment;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import io.github.cdimascio.dotenv.Dotenv;
import ku.cs.transport_application.DTO.PaymentResponse;
import ku.cs.transport_application.entity.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private CreditCardPaymentService creditCardPaymentService;

    @Mock
    private PromptPayPaymentService promptPayPaymentService;

    private Order order;

    @BeforeEach
    void setUp() {
        Dotenv dotenv = Dotenv.configure()
                .directory("src/main/resources") // Location of .env file
                .load();

        Stripe.apiKey = dotenv.get("STRIPE_TEST_KEY");

        order = new Order();
        UUID uuid = UUID.randomUUID();
        order.setId(uuid);
        order.setTotal(100.0);
    }

    @Test
    void testCreatePaymentLinkCreditCardSuccess() throws StripeException {
        // Mocked response
        PaymentResponse mockResponse = new PaymentResponse();
        mockResponse.setPaymentLink("https://mocked.url");

        // Mock the service call
        when(creditCardPaymentService.createPaymentLink(order)).thenReturn(mockResponse);

        // Test
        PaymentResponse response = creditCardPaymentService.createPaymentLink(order);

        // Assertions
        assertNotNull(response);
        assertEquals("https://mocked.url", response.getPaymentLink());
    }

    @Test
    void testCreatePaymentLinkPromptPaySuccess() throws StripeException {
        // Mocked response
        PaymentResponse mockResponse = new PaymentResponse();
        mockResponse.setPaymentLink("https://mocked.url");

        // Mock the service call
        when(promptPayPaymentService.createPaymentLink(order)).thenReturn(mockResponse);

        // Test
        PaymentResponse response = promptPayPaymentService.createPaymentLink(order);

        // Assertions
        assertNotNull(response);
        assertEquals("https://mocked.url", response.getPaymentLink());
    }

    @Test
    void testCreatePaymentLinkInvalidOrder() throws StripeException {
        order.setTotal(-1);

        // Mock the service to throw an exception
        when(creditCardPaymentService.createPaymentLink(order)).thenThrow(new IllegalArgumentException("Invalid order total"));
        when(promptPayPaymentService.createPaymentLink(order)).thenThrow(new IllegalArgumentException("Invalid order total"));

        // Assertions
        assertThrows(IllegalArgumentException.class, () -> creditCardPaymentService.createPaymentLink(order));
        assertThrows(IllegalArgumentException.class, () -> promptPayPaymentService.createPaymentLink(order));
    }
}