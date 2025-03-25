package ku.cs.transport_application.service;

import ku.cs.transport_application.DTO.TransportationWorkerDTO;
import ku.cs.transport_application.common.TransportationWorkerStatus;
import ku.cs.transport_application.entity.TransportationWorker;
import ku.cs.transport_application.repository.TransportationWorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransportationWorkerService {

    @Autowired
    TransportationWorkerRepository transportationWorkerRepository;

    public List<TransportationWorkerDTO> getAvailableTransportationWorker() {
        List<TransportationWorker> workers = transportationWorkerRepository.findByStatus(TransportationWorkerStatus.AVAILABLE);
        return workers.stream().map(worker -> {
            TransportationWorkerDTO dto = new TransportationWorkerDTO();
            dto.setId(worker.getId());
            dto.setUsername(worker.getUsername());
            dto.setName(worker.getName());
            dto.setPhoneNumber(worker.getPhoneNumber());
            dto.setEmail(worker.getEmail());
            dto.setStatus(worker.getStatus());
            return dto;
        }).collect(Collectors.toList());
    }

    public void updateTransportationWorker(UUID workerId, TransportationWorkerStatus status) {
        TransportationWorker record = transportationWorkerRepository.findById(workerId)
                .orElseThrow(() -> new IllegalArgumentException("Worker not found"));
        record.setStatus(status);
        transportationWorkerRepository.save(record);
    }

    public TransportationWorker findWorkerByUsername(String username) {
        return transportationWorkerRepository.findByUsername(username);
    }

    public TransportationWorkerDTO findWorkerByIdWithDTO(UUID workerId) {
        TransportationWorker record = transportationWorkerRepository.findById(workerId)
                .orElseThrow(() -> new IllegalArgumentException("Worker not found"));
        TransportationWorkerDTO dto = new TransportationWorkerDTO();
        dto.setId(workerId);
        dto.setUsername(record.getUsername());
        dto.setName(record.getName());
        dto.setPhoneNumber(record.getPhoneNumber());
        dto.setEmail(record.getEmail());
        dto.setProfilePicture(record.getProfilePicture());
        dto.setStatus(record.getStatus());
        return dto;
    }

    public TransportationWorker findWorkerById(UUID workerId) {
        return transportationWorkerRepository.findById(workerId)
                .orElseThrow(() -> new IllegalArgumentException("Worker not found"));
    }

    public void setTransportationWorker(TransportationWorker worker) {
        transportationWorkerRepository.save(worker);
    }
}
