package com.nardi.devicemanager.service;

import com.nardi.devicemanager.entity.Device;
import com.nardi.devicemanager.entity.DeviceState;
import com.nardi.devicemanager.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DeviceService {
    private final DeviceRepository deviceRepository;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public Device createDevice(Device device) {
        device.setId(null);
        device.setCreationTime(LocalDateTime.now());
        return deviceRepository.save(device);
    }

    public Optional<Device> getDevice(Long id) {
        return deviceRepository.findById(id);
    }

    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    public List<Device> getDevicesByBrand(String brand) {
        return deviceRepository.findByBrand(brand);
    }

    public List<Device> getDevicesByState(DeviceState state) {
        return deviceRepository.findByState(state);
    }

    @Transactional
    public Device updateDevice(Long id, Device updatedDevice, boolean partial) {
        Device existing = deviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Device not found"));
        if (!existing.getCreationTime().equals(updatedDevice.getCreationTime())) {
            throw new IllegalArgumentException("Creation time cannot be updated");
        }
        if (existing.getState() == DeviceState.IN_USE) {
            if (!existing.getName().equals(updatedDevice.getName()) ||
                !existing.getBrand().equals(updatedDevice.getBrand())) {
                throw new IllegalArgumentException("Name and brand cannot be updated if device is in use");
            }
        }
        if (!partial || updatedDevice.getName() != null) existing.setName(updatedDevice.getName());
        if (!partial || updatedDevice.getBrand() != null) existing.setBrand(updatedDevice.getBrand());
        if (!partial || updatedDevice.getState() != null) existing.setState(updatedDevice.getState());
        // creationTime is not updated
        return deviceRepository.save(existing);
    }

    public void deleteDevice(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Device not found"));
        if (device.getState() == DeviceState.IN_USE) {
            throw new IllegalArgumentException("In use devices cannot be deleted");
        }
        deviceRepository.deleteById(id);
    }
}

