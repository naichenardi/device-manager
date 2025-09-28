package com.nardi.devicemanager.dto;

import com.nardi.devicemanager.entity.DeviceState;
import java.time.LocalDateTime;

public class DeviceResponse {
    private Long id;
    private String name;
    private String brand;
    private DeviceState state;
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


