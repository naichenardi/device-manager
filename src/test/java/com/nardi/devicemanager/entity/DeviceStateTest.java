package com.nardi.devicemanager.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DeviceStateTest {
    @Test
    void testEnumValues() {
        // Arrange
        // (No setup needed for enum)

        // Act & Assert
        assertEquals(DeviceState.AVAILABLE, DeviceState.valueOf("AVAILABLE"));
        assertEquals(DeviceState.IN_USE, DeviceState.valueOf("IN_USE"));
        assertEquals(DeviceState.INACTIVE, DeviceState.valueOf("INACTIVE"));
        assertEquals(3, DeviceState.values().length);
    }
}

