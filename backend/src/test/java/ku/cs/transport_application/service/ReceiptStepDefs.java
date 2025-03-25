package ku.cs.transport_application.service;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ku.cs.transport_application.common.OrderStatus;
import ku.cs.transport_application.entity.Order;
import ku.cs.transport_application.entity.TransportationWorker;
import ku.cs.transport_application.entity.User;
import ku.cs.transport_application.response.ReceiptResponse;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Collections;

public class ReceiptStepDefs {
    private ReceiptService receiptService;
    private Order order;
    private Order order2;
    private User dummyUser;
    private ReceiptResponse receiptResponse;
    private Exception exception;
    private String currentUser;
    private String currentRole;

    @Before
    public void setUp() {
        receiptService = mock(ReceiptService.class);
    }

    @Given("an order for customer {string}")
    public void anOrderForCustomer(String customerName){
        order = new Order();
        order.setId(UUID.randomUUID());
        order.setCustomerName(customerName);
        order.setDate(LocalDateTime.now());
        order.setStatus(OrderStatus.COMPLETED);
        order.setCustomerAddress("Dummy Address");

        TransportationWorker dummyWorker = new TransportationWorker();
        dummyWorker.setName("Dummy Worker");
        dummyWorker.setPhoneNumber("0123456789");
        dummyWorker.setEmail("worker@example.com");
        order.setWorker(dummyWorker);

        dummyUser = new User();
        dummyUser.setName(customerName);
        order.setUser(dummyUser);
    }

    @Given("I am logged in as admin")
    public void iAmLoggedInAsAdmin() {
        currentRole = "admin";
        currentUser = "admin";
        mockReceiptResponse(true);
    }

    @Given("I am logged in as customer {string}")
    public void iAmLoggedInAsCustomer(String customerName) {
        currentRole = "customer";
        currentUser = customerName;
        boolean isAuthorized = order != null && order.getCustomerName().equals(currentUser);
        mockReceiptResponse(isAuthorized);
    }

    private void mockReceiptResponse(boolean isAuthorized) {
        if (order == null) return;

        if (isAuthorized) {
            when(receiptService.getReceiptByOrderId(order.getId()))
                    .thenReturn(new ReceiptResponse(
                            order.getId(),
                            order.getDate(),
                            order.getStatus(),
                            order.getCustomerName(),
                            order.getCustomerAddress(),
                            order.getWorker().getName(),
                            order.getWorker().getPhoneNumber(),
                            order.getWorker().getEmail(),
                            order.getUser().getName(),
                            Collections.emptyList()
                    ));
        } else {
            when(receiptService.getReceiptByOrderId(order.getId()))
                    .thenThrow(new IllegalArgumentException("Unauthorized access"));
        }
    }

    @When("I view the receipt")
    public void iViewTheReceipt() {
        try {
            if (!order.getCustomerName().equals(currentUser) && !order.getCustomerName().equals("admin")) {
                exception = new IllegalArgumentException();
            }
            receiptResponse = receiptService.getReceiptByOrderId(order.getId());
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("the receipt should be returned successfully and display the customer name {string}")
    public void theReceiptShouldBeReturnedSuccessfully(String expectedCustomerName) {
        assertNotNull(receiptResponse, "Expected a receipt response, but got null.");
        assertEquals(expectedCustomerName, receiptResponse.getCus_name());
    }

    @Then("the receipt should be returned unsuccessfully")
    public void thenTheReceiptShouldBeReturnedUnsuccessfully() {
        assertNull(receiptResponse);
    }

    @And("I have placed an order successfully")
    public void iOrderSuccessfulWithOrderId() {
        order.setStatus(OrderStatus.COMPLETED);
    }

    @Then("the receipt should return orderID same as the one I ordered")
    public void theReceiptShouldReturnOrderIDSameAsTheOneIOrdered() {
        assertEquals(order.getId(), order.getReceipt().getReceiptId());
    }

    @Given("a new order for customer {string}")
    public void aNewOrderForCustomer(String customerName){
        order2 = new Order();
        order2.setId(UUID.randomUUID());
        order2.setCustomerName(customerName);
        order2.setDate(LocalDateTime.now());
        order2.setStatus(OrderStatus.COMPLETED);
        order2.setCustomerAddress("Dummy Address");

        TransportationWorker dummyWorker = new TransportationWorker();
        dummyWorker.setName("Dummy Worker");
        dummyWorker.setPhoneNumber("0123456789");
        dummyWorker.setEmail("worker@example.com");
        order2.setWorker(dummyWorker);

        User dummyUser = new User();
        dummyUser.setName(customerName);
        order2.setUser(dummyUser);
    }

    @Then("the system should generate a unique receipt number for the order")
    public void theSystemShouldGenerateAUniqueReceiptNumberForTheOrder() {
        assertNotEquals(order.getReceipt().getReceiptId(), order2.getReceipt().getReceiptId());
    }

    @Then("the receipt should display the correct order details and total amount")
    public void theReceiptShouldDisplayTheCorrectOrderDetailsAndTotalAmount() {
        assertEquals(order.getUser(),dummyUser.getUsername());
        assertEquals(order.getId(), dummyUser.getId());
    }
}
