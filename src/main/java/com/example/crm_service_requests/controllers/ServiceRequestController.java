package com.example.crm_service_requests.controllers;

import com.example.crm_service_requests.entity.Priority;
import com.example.crm_service_requests.entity.ServiceRequest;
import com.example.crm_service_requests.entity.Status;
import com.example.crm_service_requests.security.JwtConfig;
import com.example.crm_service_requests.service.ServiceRequestService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin(origins = "http://localhost:4200")
public class ServiceRequestController {
    @Autowired
    private ServiceRequestService serviceRequestService;

    private final JwtConfig jwtConfig;

    @Autowired
    public ServiceRequestController(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @GetMapping("/analytics")
    public ResponseEntity<?> getAllServiceRequests(){
        List<ServiceRequest> requests = serviceRequestService.getAllServiceRequests( false, null, null, null, null);

        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllServiceRequests(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) Integer location
            /*@RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,*/
    ) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");

            Claims claims = Jwts.parser().setSigningKey(jwtConfig.getSecretKey()).parseClaimsJws(token).getBody();
            Long userId = claims.get("userId", Long.class);
            List<String> userRoles = claims.get("roles", List.class);

            if(userId == null) throw new RuntimeException("Invalid user");
            if(userRoles == null) throw new RuntimeException("Invalid roles");

            try{
                List<ServiceRequest> requests = serviceRequestService.getAllServiceRequests( userRoles.contains("ROLE_USER"), status, priority, location, userId);

                return new ResponseEntity<>(requests, HttpStatus.OK);
            } catch (Exception exception) {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception exception) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceRequest> getServiceRequestById(@PathVariable Long id) {
        Optional<ServiceRequest> serviceRequest = serviceRequestService.getServiceRequestById(id);
        return serviceRequest.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createServiceRequest(@RequestBody ServiceRequest serviceRequest, @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");

            Claims claims = Jwts.parser().setSigningKey(jwtConfig.getSecretKey()).parseClaimsJws(token).getBody();
            long user_id = claims.get("userId", Long.class);

            try{
                serviceRequest.setCreatedByUser(user_id);
                ServiceRequest createdRequest = serviceRequestService.createServiceRequest(serviceRequest);

                if(Arrays.asList(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16}).contains(createdRequest.getType()))
                    serviceRequestService.handleServiceRequest(createdRequest);

                return ResponseEntity.ok("Service request created successfully");
            } catch (Exception exception) {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception exception) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateServiceRequest(@RequestBody ServiceRequest serviceRequest, @RequestHeader("Authorization") String authorizationHeader) {
        try {

            String token = authorizationHeader.replace("Bearer ", "");
            Claims claims = Jwts.parser().setSigningKey(jwtConfig.getSecretKey()).parseClaimsJws(token).getBody();
            Long userId = claims.get("userId", Long.class);
            List<String> userRoles = claims.get("roles", List.class);

            if(userId == null) throw new RuntimeException("Invalid user");
            if(userRoles == null) throw new RuntimeException("Invalid roles");
            if(userRoles.contains("ROLE_USER") || userRoles.contains("ROLE_SUPPORT")) throw new RuntimeException("Forbidden action");

            try {
                if(serviceRequest.getUpdateDate() == null) serviceRequest.setUpdateDate(LocalDateTime.now());

                if(serviceRequest.getStatus() == Status.COMPLETED)
                    serviceRequest.setCompletedDate(LocalDateTime.now());

                serviceRequestService.updateServiceRequest(serviceRequest);
                return ResponseEntity.ok("Service request updated successfully");
            } catch (RuntimeException e) {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception exception) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceRequest(@PathVariable Long id) {
        serviceRequestService.deleteServiceRequest(id);
        return ResponseEntity.noContent().build();
    }


}
