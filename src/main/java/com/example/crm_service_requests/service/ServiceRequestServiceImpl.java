package com.example.crm_service_requests.service;

import com.example.crm_service_requests.entity.*;
import com.example.crm_service_requests.entity.ServiceRequest;
import com.example.crm_service_requests.repository.ServiceRequestRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceRequestServiceImpl implements ServiceRequestService{
    @Autowired
    private ServiceRequestRepo serviceRequestRepository;

    @Autowired
    private EmailClientService emailService;

    @Override
    public List<ServiceRequest> getAllServiceRequests(boolean isUser, Status status, Priority priority, Integer location, Long createdBy) {
        List<ServiceRequest> requests = serviceRequestRepository.findServiceRequests(isUser, status, priority, location,  createdBy);

        return requests.stream()
                .sorted(Comparator.comparing(ServiceRequest::getStatus, Comparator.naturalOrder())
                        .thenComparing(ServiceRequest::getPriority, Comparator.naturalOrder())
                        .thenComparing(ServiceRequest::getCreationDate)
                        .thenComparing(ServiceRequest::getUpdateDate, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ServiceRequest> getServiceRequestById(Long id) {
        return serviceRequestRepository.findById(id);
    }

    @Override
    public ServiceRequest createServiceRequest(ServiceRequest serviceRequest) {
        return serviceRequestRepository.save(serviceRequest);
    }

    @Override
    public void updateServiceRequest(ServiceRequest serviceRequest) {
        Optional<ServiceRequest> existingRequest = serviceRequestRepository.findById(serviceRequest.getId());

        if(existingRequest.isEmpty()) throw new RuntimeException("Service request not found with id " + serviceRequest.getId());
/*
        if (existingRequest.isPresent()) {
            ServiceRequest updatedRequest = existingRequest.get();
            updatedRequest.setDescription(serviceRequest.getDescription());
            updatedRequest.setLocation(serviceRequest.getLocation());
            updatedRequest.setPriority(serviceRequest.getPriority());
            updatedRequest.setStatus(serviceRequest.getStatus());
            updatedRequest.setCreationDate(serviceRequest.getCreationDate());
            updatedRequest.setCreatedByUser(serviceRequest.getCreatedByUser());
            updatedRequest.setAddress(serviceRequest.getAddress());
            serviceRequestRepository.save(updatedRequest);
        } else {
            throw new RuntimeException("Service request not found with id " + serviceRequest.getId());
        }*/

        BeanUtils.copyProperties(serviceRequest, existingRequest.get(), "id", "description", "creation_date");
        serviceRequestRepository.saveAndFlush(existingRequest.get());
    }

    @Override
    public void deleteServiceRequest(Long id) {
        serviceRequestRepository.deleteById(id);
    }

    @Override
    public void handleServiceRequest(ServiceRequest serviceRequest) {
        switch (serviceRequest.getType()) {
            case 1,2,3,4,5,6,7,8,9,10,11,12,13:
                handleCityHall(serviceRequest);
                break;
            case 14,15:
                handlePassports(serviceRequest);
                break;
            case 16:
                handlePolice(serviceRequest);
                break;
            default:
        }
    }

    private void handleCityHall(ServiceRequest serviceRequest) {
        String messageBody = buildMessageBody(serviceRequest.getId(),
                serviceRequest.getDescription(),
                serviceRequest.getAddress(),
                serviceRequest.getPriority().toString()
        );
        emailService.sendEmail(getMailByLocation(serviceRequest.getLocation()), getSubjectById(serviceRequest), messageBody);
    }

    private void handlePassports(ServiceRequest serviceRequest) {
        String messageBody = buildMessageBody(serviceRequest.getId(),
                serviceRequest.getDescription(),
                serviceRequest.getAddress(),
                serviceRequest.getPriority().toString()
        );
        //directia de pasapoarte
        emailService.sendEmail("dgp.relatiipublice@mai.gov.ro",  getSubjectById(serviceRequest), messageBody);
    }

    private void handlePolice(ServiceRequest serviceRequest) {
        String messageBody = buildMessageBody(serviceRequest.getId(), serviceRequest.getDescription(), serviceRequest.getAddress(), serviceRequest.getPriority().toString());
        //eliberare cazier
        emailService.sendEmail("politiacapitalei@b.politiaromana.ro", getSubjectById(serviceRequest), messageBody);
    }

    /*
    private void handlePassports(ServiceRequest serviceRequest) {
        String messageBody = buildMessageBody(serviceRequest.getLocation(), serviceRequest.getDescription(), serviceRequest.getAddress(), serviceRequest.getPriority().toString());
        emailService.sendEmail(getMailByLocation(serviceRequest.getLocation()), getSubjectById(serviceRequest.getType()), messageBody);
    }

    private void handleGarbageCollection(ServiceRequest serviceRequest) {
        // Add specific logic for garbage collection if needed
        emailService.sendEmail("Garbage Collection: " + serviceRequest.getSubject(), serviceRequest.getDescription());
    }

    private void handleTrafficLightMalfunction(ServiceRequest serviceRequest) {
        // Add specific logic for traffic light malfunction if needed
        emailService.sendServiceRequestEmail("Traffic Light Malfunction: " + serviceRequest.getSubject(), serviceRequest.getDescription());
    }

    private void handleGeneralInquiry(ServiceRequest serviceRequest) {
        // Add specific logic for general inquiries if needed
        emailService.sendServiceRequestEmail("General Inquiry: " + serviceRequest.getSubject(), serviceRequest.getDescription());
    }*/



    private String getSubjectById(ServiceRequest serviceRequest) {
        String subject = switch (serviceRequest.getType()) {
            case 1 -> "Street Repair";
            case 2 -> "Amenajare spatiu verde";
            case 3 -> "Cerere aviz taiere/toaletare arbori PF/PJ/AP";
            case 4 -> "Solicitare amplasare banci";
            case 5 -> "Solicitare amplasare cosuri de gunoi";
            case 6 -> "Solicitare amplasare garduri metalice";
            case 7 -> "Adeverinta bunuri desfiintate sau ridicate de pe domeniul public";
            case 8 -> "Cerere stalpisori delimitare acces pietonal";
            case 9 -> "Cerere limitatoare de viteza";
            case 10 -> "Marcaje rutiere orizontale";
            case 11 -> "Solicitare intretinere parcaj";
            case 12 -> "Cerere actualizare date de contract";
            case 13 -> "Solicitare schimbare act de identitate";
            case 14 -> "Solicitare pasaport simplu electronic";
            case 15 -> "Solicitare pasaport simplu temporar";
            case 16 -> "Eliberare cazier";

            default -> "Others";

        };

        return "REQ" + serviceRequest.getId() + " " + subject;
    }
    private String getMailByLocation(Integer location) {
        return switch (location) {
            case 1 -> "registratura@primarias1.ro";
            case 2 -> "infopublice@ps2.ro";
            case 3 -> "relatiipublice@primarie3.ro";
            case 4 -> "contact@ps4.ro";
            case 5 -> "primarie@sector5.ro";
            case 6 -> "prim6@primarie6.ro";
            default -> "relatiipublice@pmb.ro";
        };
    }

    private String buildMessageBody(Long id, String description, String address, String priority) {
        return "Id request :" + id
                + "\nDescription: " + description
                + "\nPriority" + priority
                + "\nLocation: " + address;
    }
}
