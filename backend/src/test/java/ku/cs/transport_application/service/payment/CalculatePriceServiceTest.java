package ku.cs.transport_application.service.payment;

import ku.cs.transport_application.common.ProductType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatePriceServiceTest {

    private CalculatePriceService calculatePriceService;

    @BeforeEach
    void setUp() {
        calculatePriceService = new CalculatePriceService();
    }

    @Test
    void testCalculateShippingLightweight() {
        int cost = calculatePriceService.calculateShipping(ProductType.LIGHTWEIGHT, 3);
        assertEquals(150, cost); // 50 * 3
    }

    @Test
    void testCalculateShippingHeavyweight() {
        int cost = calculatePriceService.calculateShipping(ProductType.HEAVYWEIGHT, 2);
        assertEquals(300, cost); // 150 * 2
    }

    @Test
    void testCalculateShippingFragile() {
        int cost = calculatePriceService.calculateShipping(ProductType.FRAGILE, 5);
        assertEquals(500, cost); // 100 * 5
    }

    @Test
    void testCalculateShippingHazardous() {
        int cost = calculatePriceService.calculateShipping(ProductType.HAZARDOUS, 4);
        assertEquals(800, cost); // 200 * 4
    }

    @Test
    void testCalculateShippingSpecialty() {
        int cost = calculatePriceService.calculateShipping(ProductType.SPECIALTY, 1);
        assertEquals(300, cost); // 300 * 1
    }
}
