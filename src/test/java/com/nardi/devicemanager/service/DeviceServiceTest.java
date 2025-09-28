package com.nardi.devicemanager.service;

import com.nardi.devicemanager.entity.Device;
import com.nardi.devicemanager.entity.DeviceState;
import com.nardi.devicemanager.exception.DeviceNotFoundException;
import com.nardi.devicemanager.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeviceServiceTest {
    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private DeviceService deviceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createDevice_setsIdAndCreationTimeAndSaves() {
        // Arrange
        Device device = new Device();
        device.setName("Device1");
        device.setBrand("BrandA");
        device.setState(DeviceState.AVAILABLE);
        when(deviceRepository.save(any(Device.class))).thenAnswer(i -> i.getArgument(0));
        
		// Act
        Device saved = deviceService.createDevice(device);
        
		// Assert
        assertNull(saved.getId());
        assertNotNull(saved.getCreationTime());
        verify(deviceRepository).save(saved);
    }

    @Test
    void getDevice_returnsDeviceIfExists() {
        // Arrange
        Device device = new Device();
        device.setId(1L);
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));
        
		// Act
        Device result = deviceService.getDevice(1L);
        
		// Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getDevice_returnsEmptyIfNotExists() {
        // Arrange
        when(deviceRepository.findById(2L)).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(DeviceNotFoundException.class, () -> deviceService.getDevice(2L));
    }

    @Test
    void getAllDevices_returnsList() {
        // Arrange
        List<Device> devices = Arrays.asList(new Device(), new Device());
        when(deviceRepository.findAll()).thenReturn(devices);
        
		// Act
        List<Device> result = deviceService.getAllDevices();
        
		// Assert
        assertEquals(2, result.size());
    }

    @Test
    void getDevicesByBrand_returnsList() {
        // Arrange
        List<Device> devices = List.of(new Device());
        when(deviceRepository.findByBrand("BrandA")).thenReturn(devices);
        
		// Act
        List<Device> result = deviceService.getDevicesByBrand("BrandA");
        
		// Assert
        assertEquals(1, result.size());
    }

    @Test
    void getDevicesByState_returnsList() {
        // Arrange
        List<Device> devices = List.of(new Device());
        when(deviceRepository.findByState(DeviceState.AVAILABLE)).thenReturn(devices);
        
		// Act
        List<Device> result = deviceService.getDevicesByState(DeviceState.AVAILABLE);
        
		// Assert
        assertEquals(1, result.size());
    }

    @Test
    void updateDevice_partialUpdate_success() {
        // Arrange
        Device existing = new Device();
        existing.setId(1L);
        existing.setName("Old");
        existing.setBrand("BrandA");
        existing.setState(DeviceState.AVAILABLE);
        existing.setCreationTime(LocalDateTime.now());
        Device update = new Device();
        update.setName("New");
        update.setBrand("BrandA");
        update.setState(DeviceState.AVAILABLE);
        update.setCreationTime(existing.getCreationTime());
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(deviceRepository.save(any(Device.class))).thenAnswer(i -> i.getArgument(0));
        
		// Act
        Device result = deviceService.updateDevice(1L, update, true);
        
		// Assert
        assertEquals("New", result.getName());
    }

    @Test
    void updateDevice_throwsIfInUseAndNameOrBrandChanged() {
        // Arrange
        Device existing = new Device();
        existing.setId(1L);
        existing.setName("Old");
        existing.setBrand("BrandA");
        existing.setState(DeviceState.IN_USE);
        existing.setCreationTime(LocalDateTime.now());
        Device update = new Device();
        update.setName("New");
        update.setBrand("BrandA");
        update.setState(DeviceState.IN_USE);
        update.setCreationTime(existing.getCreationTime());
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(existing));
        
		// Act & Assert
        assertThrows(IllegalArgumentException.class, () -> deviceService.updateDevice(1L, update, true));
    }

    @Test
    void deleteDevice_success() {
        // Arrange
        Device device = new Device();
        device.setId(1L);
        device.setState(DeviceState.AVAILABLE);
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));
		
		// Act
        deviceService.deleteDevice(1L);
        
		// Assert
        verify(deviceRepository).deleteById(1L);
    }

    @Test
    void deleteDevice_throwsIfInUse() {
        // Arrange
        Device device = new Device();
        device.setId(1L);
        device.setState(DeviceState.IN_USE);
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));
        
		// Act & Assert
        assertThrows(IllegalArgumentException.class, () -> deviceService.deleteDevice(1L));
    }

    @Test
    void deleteDevice_throwsIfNotFound() {
        // Arrange
        when(deviceRepository.findById(1L)).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(DeviceNotFoundException.class, () -> deviceService.deleteDevice(1L));
    }
}
