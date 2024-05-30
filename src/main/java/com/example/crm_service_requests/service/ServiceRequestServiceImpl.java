package com.example.crm_service_requests.service;

import com.example.crm_service_requests.entity.*;
import com.example.crm_service_requests.entity.ServiceRequest;
import com.example.crm_service_requests.repository.ServiceRequestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceRequestServiceImpl implements ServiceRequestService{
    @Autowired
    private ServiceRequestRepo serviceRequestRepository;

    public List<ServiceRequest> getAllServiceRequests(Status status, Priority priority, String city, Long createdBy) {
        return serviceRequestRepository.findServiceRequests(status, priority, city, createdBy);
    }

    public Optional<ServiceRequest> getServiceRequestById(Long id) {
        return serviceRequestRepository.findById(id);
    }

    public ServiceRequest createServiceRequest(ServiceRequest serviceRequest) {
        return serviceRequestRepository.save(serviceRequest);
    }

    public ServiceRequest updateServiceRequest(Long id, ServiceRequest serviceRequest) {
        Optional<ServiceRequest> existingRequest = serviceRequestRepository.findById(id);
        if (existingRequest.isPresent()) {
            ServiceRequest updatedRequest = existingRequest.get();
            updatedRequest.setDescription(serviceRequest.getDescription());
            updatedRequest.setLocation(serviceRequest.getLocation());
            updatedRequest.setPriority(serviceRequest.getPriority());
            updatedRequest.setStatus(serviceRequest.getStatus());
            updatedRequest.setCreationDate(serviceRequest.getCreationDate());
            updatedRequest.setCreated_by_user(serviceRequest.getCreated_by_user());
            updatedRequest.setCity(serviceRequest.getCity());
            updatedRequest.setStreet(serviceRequest.getStreet());
            return serviceRequestRepository.save(updatedRequest);
        } else {
            throw new RuntimeException("Service request not found with id " + id);
        }
    }

    public void deleteServiceRequest(Long id) {
        serviceRequestRepository.deleteById(id);
    }
}
