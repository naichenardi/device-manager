package com.nardi.devicemanager.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nardi.devicemanager.entity.DeviceState;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;


class DeviceResponseTest {
    @Test
    void testJsonSerializationDeserialization() throws Exception {
        // Arrange
        DeviceResponse resp = new DeviceResponse();
        resp.setId(123L);
        resp.setName("DeviceName");
        resp.setBrand("BrandName");
        resp.setState(DeviceState.IN_USE);
        LocalDateTime now = LocalDateTime.now().withNano(0); // Truncate nanos for comparison
        resp.setCreationTime(now);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        // Act
        String json = mapper.writeValueAsString(resp);
        DeviceResponse deserialized = mapper.readValue(json, DeviceResponse.class);

        // Assert
        assertEquals(resp.getId(), deserialized.getId());
        assertEquals(resp.getName(), deserialized.getName());
        assertEquals(resp.getBrand(), deserialized.getBrand());
        assertEquals(resp.getState(), deserialized.getState());
        assertEquals(resp.getCreationTime(), deserialized.getCreationTime());
    }
}
