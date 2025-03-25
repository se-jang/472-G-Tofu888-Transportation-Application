package ku.cs.transport_application.service;

import com.stripe.exception.StripeException;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ku.cs.transport_application.entity.Order;
import ku.cs.transport_application.DTO.PaymentResponse;
import ku.cs.transport_application.service.payment.CreditCardPaymentService;
import ku.cs.transport_application.service.payment.PromptPayPaymentService;

import java.util.UUID;

public class PaymentStepDefs {
    private CreditCardPaymentService creditCardPaymentService;
    private PromptPayPaymentService promptPayPaymentService;
    private Order order;
    private PaymentResponse responseCredit;
    private PaymentResponse responsePrompt;
    private Exception exception;

    @Before
    public void setUp() {
        creditCardPaymentService = mock(CreditCardPaymentService.class);
        promptPayPaymentService = mock(PromptPayPaymentService.class);
    }

    @Given("a valid order with total {double}")
    public void aValidOrderWithTotal(double total) throws StripeException {
        order = createOrder(total);
        mockPaymentLinkCreation(true);
    }

    @Given("an invalid order with total {double}")
    public void anInvalidOrderWithTotal(double total) throws StripeException {
        order = createOrder(total);
        mockPaymentLinkCreation(false);
    }

    private Order createOrder(double total) {
        Order newOrder = new Order();
        newOrder.setId(UUID.randomUUID());
        newOrder.setTotal(total);
        return newOrder;
    }

    private void mockPaymentLinkCreation(boolean isValid) throws StripeException {
        if (isValid) {
            when(creditCardPaymentService.createPaymentLink(order))
                    .thenReturn(createMockPaymentResponse("https://payment.link/credit"));
            when(promptPayPaymentService.createPaymentLink(order))
                    .thenReturn(createMockPaymentResponse("https://payment.link/prompt"));
        } else {
            when(creditCardPaymentService.createPaymentLink(order))
                    .thenThrow(new IllegalArgumentException("Invalid order total"));
            when(promptPayPaymentService.createPaymentLink(order))
                    .thenThrow(new IllegalArgumentException("Invalid order total"));
        }
    }

    private PaymentResponse createMockPaymentResponse(String link) {
        PaymentResponse response = new PaymentResponse();
        response.setPaymentLink(link);
        return response;
    }

    @When("I create a payment link")
    public void iCreateAPaymentLink() {
        try {
            responseCredit = creditCardPaymentService.createPaymentLink(order);
            responsePrompt = promptPayPaymentService.createPaymentLink(order);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("the payment link should be returned successfully")
    public void thePaymentLinkShouldBeReturnedSuccessfully() {
        assertNotNull(responseCredit, "Expected a payment response, but got null.");
        assertNotNull(responseCredit.getPaymentLink(), "Expected a payment URL, but got null.");
        assertNotNull(responsePrompt, "Expected a payment response, but got null.");
        assertNotNull(responsePrompt.getPaymentLink(), "Expected a payment URL, but got null.");
    }

    @Then("it should throw an IllegalArgumentException")
    public void itShouldThrowAnIllegalArgumentException() {
        assertNotNull(exception, "Expected an exception, but got null.");
        assertInstanceOf(IllegalArgumentException.class, exception, "Expected IllegalArgumentException, but got " + exception.getClass());
    }
}