package com.nardi.devicemanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nardi.devicemanager.dto.DeviceRequest;
import com.nardi.devicemanager.entity.Device;
import com.nardi.devicemanager.entity.DeviceState;
import com.nardi.devicemanager.service.DeviceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
            return Mockito.mock(DeviceService.class);
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
        Mockito.when(deviceService.createDevice(any(Device.class))).thenReturn(device);

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
        Mockito.when(deviceService.getDevice(99L)).thenReturn(Optional.empty());
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
    @DisplayName("PUT /api/devices/{id} - service throws IllegalArgumentException triggers exception handler")
    void updateDevice_illegalArgument() throws Exception {
        DeviceRequest request = new DeviceRequest();
        request.setName("Test Device");
        request.setBrand("BrandA");
        request.setState(DeviceState.AVAILABLE);
        Mockito.when(deviceService.updateDevice(eq(1L), any(Device.class), eq(false)))
                .thenThrow(new IllegalArgumentException("Invalid update!"));
        mockMvc.perform(put("/api/devices/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid update!"));
    }

    @Test
    @DisplayName("DELETE /api/devices/{id} - service throws generic Exception triggers 500 handler")
    void deleteDevice_internalError() throws Exception {
        Mockito.doThrow(new RuntimeException("Unexpected error")).when(deviceService).deleteDevice(1L);
        mockMvc.perform(delete("/api/devices/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal server error"));
    }
}
