package com.example.crm_service_requests.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="servicerequests")
@Getter
@Setter
public class ServiceRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type")
    private Integer type;

    @Column(name = "description")
    private String description;

    @Column(name = "location")
    private Integer location;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private Priority priority = Priority.LOW;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.PENDING;

    @Column(name = "creation_date")
    private LocalDateTime creationDate = LocalDateTime.now();

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "completed_date")
    private LocalDateTime completedDate;

    @Column(name = "address")
    private String address;

    @Column(name = "observations", columnDefinition = "text")
    private String observations;

    @JoinColumn(name = "created_by_user", nullable = false)
    private long createdByUser;

    // Getters and setters
}