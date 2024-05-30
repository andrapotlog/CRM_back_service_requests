package com.example.crm_service_requests.repository;

import com.example.crm_service_requests.entity.Priority;
import com.example.crm_service_requests.entity.ServiceRequest;
import com.example.crm_service_requests.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRequestRepo extends JpaRepository<ServiceRequest, Long> {

    @Query("SELECT sr FROM ServiceRequest sr WHERE " +
            "(:status IS NULL OR sr.status = :status) AND " +
            "(:priority IS NULL OR sr.priority = :priority) AND " +
            "(:createdBy IS NULL OR sr.created_by_user = :createdBy) AND " +
            "(:city IS NULL OR LOWER(sr.city) LIKE LOWER(CONCAT('%', :city, '%')))")
    List<ServiceRequest> findServiceRequests(Status status, Priority priority, String city, Long createdBy);
}
