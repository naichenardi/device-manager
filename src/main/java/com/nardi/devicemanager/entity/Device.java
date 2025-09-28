package com.nardi.devicemanager.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "devices")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String brand;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeviceState state;

    @Column(name = "creation_time", nullable = false, updatable = false)
    private LocalDateTime creationTime;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public DeviceState getState() { return state; }
    public void setState(DeviceState state) { this.state = state; }
    public LocalDateTime getCreationTime() { return creationTime; }
    public void setCreationTime(LocalDateTime creationTime) { this.creationTime = creationTime; }
}
