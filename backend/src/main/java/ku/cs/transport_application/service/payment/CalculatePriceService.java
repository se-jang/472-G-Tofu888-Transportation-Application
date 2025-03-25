package ku.cs.transport_application.service.payment;

import ku.cs.transport_application.common.ProductType;

public class CalculatePriceService {
    public int calculateShipping(ProductType productType, int quantity) {
        int shippingCostPerItem = switch (productType) {
            case LIGHTWEIGHT -> 50;
            case HEAVYWEIGHT -> 150;
            case FRAGILE -> 100;
            case HAZARDOUS -> 200;
            case SPECIALTY -> 300;
            default -> throw new IllegalArgumentException("Unknown product type: " + productType);
        };

        return shippingCostPerItem * quantity;
    }
}
