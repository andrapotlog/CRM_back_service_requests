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
            "(CASE WHEN :isUser = false THEN true ELSE (:createdBy IS NULL OR sr.createdByUser = :createdBy) END) AND " +
            "(:location IS NULL OR sr.location = :location)")
    List<ServiceRequest> findServiceRequests(boolean isUser, Status status, Priority priority, Integer location, Long createdBy);
}
