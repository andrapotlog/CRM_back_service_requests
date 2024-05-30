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
    private String type;

    @Column(name = "description")
    private String description;

    @Column(name = "location")
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private Priority priority = Priority.LOW;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.PENDING;

    @Column(name = "creation_date")
    private LocalDateTime creationDate = LocalDateTime.now();

    @JoinColumn(name = "created_by_user", nullable = false)
    private long created_by_user;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    // Getters and setters
}