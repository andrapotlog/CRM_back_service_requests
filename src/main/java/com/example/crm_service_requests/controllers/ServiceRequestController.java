package com.example.crm_service_requests.controllers;

import com.example.crm_service_requests.entity.Priority;
import com.example.crm_service_requests.entity.ServiceRequest;
import com.example.crm_service_requests.entity.Status;
import com.example.crm_service_requests.service.ServiceRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin(origins = "http://localhost:4200")
public class ServiceRequestController {
    @Autowired
    private ServiceRequestService serviceRequestService;

    @GetMapping
    public List<ServiceRequest> getAllServiceRequests(
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) String city,
            /*@RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,*/
            @RequestParam(required = false) Long createdBy
    ) {
        return serviceRequestService.getAllServiceRequests(status, priority, city, createdBy);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceRequest> getServiceRequestById(@PathVariable Long id) {
        Optional<ServiceRequest> serviceRequest = serviceRequestService.getServiceRequestById(id);
        return serviceRequest.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ServiceRequest> createServiceRequest(@RequestBody ServiceRequest serviceRequest) {
        ServiceRequest createdServiceRequest = serviceRequestService.createServiceRequest(serviceRequest);
        return ResponseEntity.ok(createdServiceRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceRequest> updateServiceRequest(@PathVariable Long id, @RequestBody ServiceRequest serviceRequest) {
        try {
            ServiceRequest updatedServiceRequest = serviceRequestService.updateServiceRequest(id, serviceRequest);
            return ResponseEntity.ok(updatedServiceRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceRequest(@PathVariable Long id) {
        serviceRequestService.deleteServiceRequest(id);
        return ResponseEntity.noContent().build();
    }
}
