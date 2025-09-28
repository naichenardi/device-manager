package com.nardi.devicemanager.controller;

import com.nardi.devicemanager.dto.DeviceRequest;
import com.nardi.devicemanager.dto.DeviceResponse;
import com.nardi.devicemanager.entity.Device;
import com.nardi.devicemanager.entity.DeviceState;
import com.nardi.devicemanager.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

// Future improvements:
// - Add validation for DeviceRequest using @Valid and handle validation errors.
// - Use a mapping library (e.g., MapStruct) for DTO/entity conversion.
// - Add pagination to getAllDevices.
// - Add API documentation (Swagger/OpenAPI).
// - Add authentication and authorization.
// - Improve error responses with standardized format.
// - Add logging for important actions and errors.

@RestController
@RequestMapping("/api/devices")
public class DeviceController {
    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping
    public ResponseEntity<DeviceResponse> createDevice(@RequestBody DeviceRequest request) {
        Device device = toEntity(request);
        Device created = deviceService.createDevice(device);
        return new ResponseEntity<>(toResponse(created), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceResponse> getDevice(@PathVariable Long id) {
        Device device = deviceService.getDevice(id);
        return ResponseEntity.ok(toResponse(device));
    }

    @GetMapping
    public List<DeviceResponse> getAllDevices(@RequestParam(required = false) String brand,
                                              @RequestParam(required = false) DeviceState state) {
        if (brand != null) {
            return deviceService.getDevicesByBrand(brand).stream().map(this::toResponse).collect(Collectors.toList());
        } else if (state != null) {
            return deviceService.getDevicesByState(state).stream().map(this::toResponse).collect(Collectors.toList());
        } else {
            return deviceService.getAllDevices().stream().map(this::toResponse).collect(Collectors.toList());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeviceResponse> updateDevice(@PathVariable Long id, @RequestBody DeviceRequest request) {
        Device existingOpt = deviceService.getDevice(id);
        Device device = toEntity(request);
        device.setId(id);
        device.setCreationTime(existingOpt.getCreationTime());
        Device updated = deviceService.updateDevice(id, device, false);
        return ResponseEntity.ok(toResponse(updated));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DeviceResponse> partialUpdateDevice(@PathVariable Long id, @RequestBody DeviceRequest request) {
        Device existingOpt = deviceService.getDevice(id);
        Device device = toEntity(request);
        device.setId(id);
        device.setCreationTime(existingOpt.getCreationTime());
        Device updated = deviceService.updateDevice(id, device, true);
        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }

    private Device toEntity(DeviceRequest request) {
        Device device = new Device();
        device.setName(request.getName());
        device.setBrand(request.getBrand());
        device.setState(request.getState());
        return device;
    }

    private DeviceResponse toResponse(Device device) {
        DeviceResponse response = new DeviceResponse();
        response.setId(device.getId());
        response.setName(device.getName());
        response.setBrand(device.getBrand());
        response.setState(device.getState());
        response.setCreationTime(device.getCreationTime());
        return response;
    }
}
