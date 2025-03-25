package ku.cs.transport_application.service.payment;

import ku.cs.transport_application.entity.History;
import ku.cs.transport_application.entity.Order;
import ku.cs.transport_application.repository.HistoryRepository;
import ku.cs.transport_application.response.HistoryResponse;
import ku.cs.transport_application.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final OrderService orderService;

    /*
     * @desc    get all histories
     * @return  list of HistoryResponse DTO
     */
    public List<HistoryResponse> getHistories() {
        try {
            return historyRepository.findAll().stream()
                    .map(this::mapToHistoryResponse)
                    .toList();
        } catch (Exception e) {
            log.error("Failed to get all histories. Error: {}", e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    /*
     * @desc    get histories by user id
     * @param   id of user to find histories
     * @return  list of HistoryResponse DTO
     */
    public List<HistoryResponse> getHistoriesByUserId(UUID userId) {
        try {
            return historyRepository.findByUserId(userId).stream()
                    .map(this::mapToHistoryResponse)
                    .toList();
        } catch (Exception e) {
            log.error("Failed to get histories by userId: {}. Error: {}", userId, e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    /*
     * @desc    get histories by order id
     * @param   id of order to find histories
     * @return  list of HistoryResponse DTO
     */
    public List<HistoryResponse> getHistoriesByOrderId(UUID orderId) {
        try {
            return historyRepository.findByOrderId(orderId).stream()
                    .map(this::mapToHistoryResponse)
                    .toList();
        } catch (Exception e) {
            log.error("Failed to get histories by orderId: {}. Error: {}", orderId, e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    /*
     * @desc    add history by order id if not exist
     * @param   create history request (order id)
     * @return  result of saving (success ? HistoryResponse : Optional.empty())
     */
    public Optional<HistoryResponse> addHistoryByOrderId(UUID orderId) {
        if (!this.getHistoriesByOrderId(orderId).isEmpty()) {
            log.warn("History already exists for orderId: {}", orderId);
            return Optional.empty();
        }

        try {
            Order order = orderService.getOrdersByOrderId(orderId);
            if (order == null) {
                log.warn("Order not found for orderId: {}", orderId);
                return Optional.empty();
            }

            History history = new History();
            history.setAmount(order.getTotal());
            history.setPaymentDate(LocalDateTime.now());
            history.setOrder(order);

            History savedHistory = historyRepository.save(history);
            return Optional.of(mapToHistoryResponse(savedHistory));
        } catch (Exception e) {
            log.error("Failed to add history for orderId: {}. Error: {}", orderId, e.getMessage(), e);
        }

        return Optional.empty();
    }

    /*
     * @desc    Map History Entity to HistoryResponse DTO
     * @param   history entity to map
     * @return  HistoryResponse DTO
     */
    private HistoryResponse mapToHistoryResponse(History history) {
        return new HistoryResponse(
                history.getOrder().getCustomerName(),
                history.getOrder().getCustomerAddress(),
                history.getOrder().getWorker().getName(),
                history.getAmount()
        );
    }
}
