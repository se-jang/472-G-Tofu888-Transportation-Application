package ku.cs.transport_application.service;

import ku.cs.transport_application.entity.Order;
import ku.cs.transport_application.entity.OrderLine;
import ku.cs.transport_application.repository.OrderLineRepository;
import ku.cs.transport_application.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderlineService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderLineRepository orderLineRepository;

    public List<OrderLine> findOrderLine() {
        return orderLineRepository.findAll();
    }

    public List<OrderLine> getOrderLineByOrderId(UUID id) {
        return orderLineRepository.findByOrderId(id);
    }

}
