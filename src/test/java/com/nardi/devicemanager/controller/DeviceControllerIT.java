package com.nardi.devicemanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nardi.devicemanager.dto.DeviceRequest;
import com.nardi.devicemanager.entity.Device;
import com.nardi.devicemanager.entity.DeviceState;
import com.nardi.devicemanager.exception.DeviceNotFoundException;
import com.nardi.devicemanager.service.DeviceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc
class DeviceControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DeviceService deviceService;

    @TestConfiguration
    static class MockConfig {
        @org.springframework.context.annotation.Bean
        public DeviceService deviceService() {
            return mock(DeviceService.class);
        }
    }

    @Test
    @DisplayName("POST /api/devices - success")
    void createDevice_success() throws Exception {
        DeviceRequest request = new DeviceRequest();
        request.setName("Test Device");
        request.setBrand("BrandA");
        request.setState(DeviceState.AVAILABLE);
        Device device = new Device();
        device.setId(1L);
        device.setName(request.getName());
        device.setBrand(request.getBrand());
        device.setState(request.getState());
        when(deviceService.createDevice(any(Device.class))).thenReturn(device);

        mockMvc.perform(post("/api/devices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Device"));
    }

    @Test
    @DisplayName("GET /api/devices/{id} - not found")
    void getDevice_notFound() throws Exception {
        when(deviceService.getDevice(99L)).thenThrow(new DeviceNotFoundException(99L));
        mockMvc.perform(get("/api/devices/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/devices/{id} - invalid id format triggers exception handler")
    void getDevice_invalidIdFormat() throws Exception {
        mockMvc.perform(get("/api/devices/notANumber"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid parameter: id"));
    }

    @Test
    @DisplayName("PUT /api/devices/{id} - not found")
    void updateDevice_illegalArgument() throws Exception {
        DeviceRequest request = new DeviceRequest();
        request.setName("Test Device");
        request.setBrand("BrandA");
        request.setState(DeviceState.AVAILABLE);
        when(deviceService.updateDevice(eq(1L), any(Device.class), eq(false)))
                .thenThrow(new IllegalArgumentException("Invalid update!"));
        when(deviceService.getDevice(1L)).thenThrow(new DeviceNotFoundException(1L));
        mockMvc.perform(put("/api/devices/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Device with id 1 not found"));
    }

    @Test
    @DisplayName("DELETE /api/devices/{id} - service throws generic Exception triggers 500 handler")
    void deleteDevice_internalError() throws Exception {
        doThrow(new RuntimeException("Unexpected error")).when(deviceService).deleteDevice(1L);
        mockMvc.perform(delete("/api/devices/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal server error"));
    }

    @Test
    @DisplayName("GET /api/devices/{id} - success")
    void getDevice_success() throws Exception {
        Device device = new Device();
        device.setId(1L);
        device.setName("Test Device");
        device.setBrand("BrandA");
        device.setState(DeviceState.AVAILABLE);
        when(deviceService.getDevice(1L)).thenReturn(device);
        mockMvc.perform(get("/api/devices/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Device"));
    }

    @Test
    @DisplayName("GET /api/devices - all devices")
    void getAllDevices_success() throws Exception {
        Device device = new Device();
        device.setId(1L);
        device.setName("Test Device");
        device.setBrand("BrandA");
        device.setState(DeviceState.AVAILABLE);
        when(deviceService.getAllDevices()).thenReturn(java.util.List.of(device));
        mockMvc.perform(get("/api/devices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @DisplayName("GET /api/devices - filter by brand")
    void getDevicesByBrand_success() throws Exception {
        Device device = new Device();
        device.setId(2L);
        device.setName("Brand Device");
        device.setBrand("BrandB");
        device.setState(DeviceState.AVAILABLE);
        when(deviceService.getDevicesByBrand("BrandB")).thenReturn(java.util.List.of(device));
        mockMvc.perform(get("/api/devices?brand=BrandB"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].brand").value("BrandB"));
    }

    @Test
    @DisplayName("GET /api/devices - filter by state")
    void getDevicesByState_success() throws Exception {
        Device device = new Device();
        device.setId(3L);
        device.setName("State Device");
        device.setBrand("BrandC");
        device.setState(DeviceState.IN_USE);
        when(deviceService.getDevicesByState(DeviceState.IN_USE)).thenReturn(java.util.List.of(device));
        mockMvc.perform(get("/api/devices?state=IN_USE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].state").value("IN_USE"));
    }

    @Test
    @DisplayName("PUT /api/devices/{id} - success")
    void updateDevice_success() throws Exception {
        DeviceRequest request = new DeviceRequest();
        request.setName("Updated Device");
        request.setBrand("BrandA");
        request.setState(DeviceState.AVAILABLE);
        Device existing = new Device();
        existing.setId(1L);
        existing.setName("Old Device");
        existing.setBrand("BrandA");
        existing.setState(DeviceState.AVAILABLE);
        Device updated = new Device();
        updated.setId(1L);
        updated.setName("Updated Device");
        updated.setBrand("BrandA");
        updated.setState(DeviceState.AVAILABLE);
        when(deviceService.getDevice(1L)).thenReturn(existing);
        when(deviceService.updateDevice(eq(1L), any(Device.class), eq(false))).thenReturn(updated);
        mockMvc.perform(put("/api/devices/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Device"));
    }

    @Test
    @DisplayName("PATCH /api/devices/{id} - success")
    void partialUpdateDevice_success() throws Exception {
        DeviceRequest request = new DeviceRequest();
        request.setName("Patched Device");
        Device existing = new Device();
        existing.setId(1L);
        existing.setName("Old Device");
        existing.setBrand("BrandA");
        existing.setState(DeviceState.AVAILABLE);
        Device updated = new Device();
        updated.setId(1L);
        updated.setName("Patched Device");
        updated.setBrand("BrandA");
        updated.setState(DeviceState.AVAILABLE);
        when(deviceService.getDevice(1L)).thenReturn(existing);
        when(deviceService.updateDevice(eq(1L), any(Device.class), eq(true))).thenReturn(updated);
        mockMvc.perform(patch("/api/devices/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Patched Device"));
    }

    @Test
    @DisplayName("DELETE /api/devices/{id} - success")
    void deleteDevice_success() throws Exception {
        doNothing().when(deviceService).deleteDevice(1L);
        mockMvc.perform(delete("/api/devices/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/devices/{id} - not found")
    void deleteDevice_notFound() throws Exception {
        doThrow(new DeviceNotFoundException(1L)).when(deviceService).deleteDevice(1L);
        mockMvc.perform(delete("/api/devices/1"))
                .andExpect(status().isNotFound());
    }
}
