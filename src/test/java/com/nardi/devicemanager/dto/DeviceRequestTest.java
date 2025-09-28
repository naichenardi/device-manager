package com.nardi.devicemanager.dto;

import com.nardi.devicemanager.entity.DeviceState;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DeviceRequestTest {
    @Test
    void testNameGetterSetter() {
        // Arrange
        DeviceRequest req = new DeviceRequest();
        // Act
        req.setName("TestDevice");
        // Assert
        assertEquals("TestDevice", req.getName());
    }

    @Test
    void testBrandGetterSetter() {
        // Arrange
        DeviceRequest req = new DeviceRequest();
        // Act
        req.setBrand("TestBrand");
        // Assert
        assertEquals("TestBrand", req.getBrand());
    }

    @Test
    void testStateGetterSetter() {
        // Arrange
        DeviceRequest req = new DeviceRequest();
        // Act
        req.setState(DeviceState.AVAILABLE);
        // Assert
        assertEquals(DeviceState.AVAILABLE, req.getState());
    }
}
