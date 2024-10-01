package com.example.crm_service_requests.service;

import com.example.crm_service_requests.entity.Priority;
import com.example.crm_service_requests.entity.ServiceRequest;
import com.example.crm_service_requests.entity.Status;

import java.util.List;
import java.util.Optional;

public interface ServiceRequestService {
    public List<ServiceRequest> getAllServiceRequests(boolean isUser, Status status, Priority priority, Integer location, Long createdBy);
    public Optional<ServiceRequest> getServiceRequestById(Long id);
    public ServiceRequest createServiceRequest(ServiceRequest serviceRequest);
    public void updateServiceRequest(ServiceRequest serviceRequest);
    public void deleteServiceRequest(Long id);
    public void handleServiceRequest(ServiceRequest serviceRequest);
}
