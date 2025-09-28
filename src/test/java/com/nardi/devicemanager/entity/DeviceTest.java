package com.nardi.devicemanager.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class DeviceTest {
    @Test
    void testGettersAndSetters() {
        // Arrange
        Device device = new Device();
        Long id = 1L;
        String name = "Device1";
        String brand = "BrandA";
        DeviceState state = DeviceState.AVAILABLE;
        LocalDateTime creationTime = LocalDateTime.now();

        // Act
        device.setId(id);
        device.setName(name);
        device.setBrand(brand);
        device.setState(state);
        device.setCreationTime(creationTime);

        // Assert
        assertEquals(id, device.getId());
        assertEquals(name, device.getName());
        assertEquals(brand, device.getBrand());
        assertEquals(state, device.getState());
        assertEquals(creationTime, device.getCreationTime());
    }
}