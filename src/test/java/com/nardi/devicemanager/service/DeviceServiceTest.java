package com.nardi.devicemanager.service;

import com.nardi.devicemanager.entity.Device;
import com.nardi.devicemanager.entity.DeviceState;
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
        Device device = new Device();
        device.setName("Device1");
        device.setBrand("BrandA");
        device.setState(DeviceState.AVAILABLE);
        when(deviceRepository.save(any(Device.class))).thenAnswer(i -> i.getArgument(0));

        Device saved = deviceService.createDevice(device);
        assertNull(saved.getId());
        assertNotNull(saved.getCreationTime());
        verify(deviceRepository).save(saved);
    }

    @Test
    void getDevice_returnsDeviceIfExists() {
        Device device = new Device();
        device.setId(1L);
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));
        Optional<Device> result = deviceService.getDevice(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void getDevice_returnsEmptyIfNotExists() {
        when(deviceRepository.findById(2L)).thenReturn(Optional.empty());
        Optional<Device> result = deviceService.getDevice(2L);
        assertFalse(result.isPresent());
    }

    @Test
    void getAllDevices_returnsList() {
        List<Device> devices = Arrays.asList(new Device(), new Device());
        when(deviceRepository.findAll()).thenReturn(devices);
        List<Device> result = deviceService.getAllDevices();
        assertEquals(2, result.size());
    }

    @Test
    void getDevicesByBrand_returnsList() {
        List<Device> devices = List.of(new Device());
        when(deviceRepository.findByBrand("BrandA")).thenReturn(devices);
        List<Device> result = deviceService.getDevicesByBrand("BrandA");
        assertEquals(1, result.size());
    }

    @Test
    void getDevicesByState_returnsList() {
        List<Device> devices = List.of(new Device());
        when(deviceRepository.findByState(DeviceState.AVAILABLE)).thenReturn(devices);
        List<Device> result = deviceService.getDevicesByState(DeviceState.AVAILABLE);
        assertEquals(1, result.size());
    }

    @Test
    void updateDevice_partialUpdate_success() {
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
        Device result = deviceService.updateDevice(1L, update, true);
        assertEquals("New", result.getName());
    }

    @Test
    void updateDevice_throwsIfCreationTimeChanged() {
        Device existing = new Device();
        existing.setId(1L);
        existing.setCreationTime(LocalDateTime.now());
        Device update = new Device();
        update.setCreationTime(existing.getCreationTime().plusDays(1));
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(existing));
        assertThrows(IllegalArgumentException.class, () -> deviceService.updateDevice(1L, update, true));
    }

    @Test
    void updateDevice_throwsIfInUseAndNameOrBrandChanged() {
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
        assertThrows(IllegalArgumentException.class, () -> deviceService.updateDevice(1L, update, true));
    }

    @Test
    void deleteDevice_success() {
        Device device = new Device();
        device.setId(1L);
        device.setState(DeviceState.AVAILABLE);
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));
        deviceService.deleteDevice(1L);
        verify(deviceRepository).deleteById(1L);
    }

    @Test
    void deleteDevice_throwsIfInUse() {
        Device device = new Device();
        device.setId(1L);
        device.setState(DeviceState.IN_USE);
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));
        assertThrows(IllegalArgumentException.class, () -> deviceService.deleteDevice(1L));
    }

    @Test
    void deleteDevice_throwsIfNotFound() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> deviceService.deleteDevice(1L));
    }
}

