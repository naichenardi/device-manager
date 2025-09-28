package com.nardi.devicemanager.repository;

import com.nardi.devicemanager.entity.Device;
import com.nardi.devicemanager.entity.DeviceState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DeviceRepositoryTest {
    @Autowired
    private DeviceRepository deviceRepository;

    @Test
    @DisplayName("Save and find device by id")
    void testSaveAndFindById() {
        // Arrange
        Device device = new Device();
        device.setName("Device1");
        device.setBrand("BrandA");
        device.setState(DeviceState.AVAILABLE);
        device.setCreationTime(LocalDateTime.now());

        // Act
        Device saved = deviceRepository.save(device);
        Optional<Device> found = deviceRepository.findById(saved.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals("Device1", found.get().getName());
    }

    @Test
    @DisplayName("Find devices by brand")
    void testFindByBrand() {
        // Arrange
        Device device = new Device();
        device.setName("Device2");
        device.setBrand("BrandB");
        device.setState(DeviceState.IN_USE);
        device.setCreationTime(LocalDateTime.now());
        deviceRepository.save(device);

        // Act
        List<Device> devices = deviceRepository.findByBrand("BrandB");

        // Assert
        assertFalse(devices.isEmpty());
        assertEquals("BrandB", devices.getFirst().getBrand());
    }

    @Test
    @DisplayName("Find devices by state")
    void testFindByState() {
        // Arrange
        Device device = new Device();
        device.setName("Device3");
        device.setBrand("BrandC");
        device.setState(DeviceState.INACTIVE);
        device.setCreationTime(LocalDateTime.now());
        deviceRepository.save(device);

        // Act
        List<Device> devices = deviceRepository.findByState(DeviceState.INACTIVE);

        // Assert
        assertFalse(devices.isEmpty());
        assertEquals(DeviceState.INACTIVE, devices.getFirst().getState());
    }
}


