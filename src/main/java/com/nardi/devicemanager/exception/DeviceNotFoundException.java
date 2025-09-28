package com.nardi.devicemanager.exception;

public class DeviceNotFoundException extends RuntimeException {
    public DeviceNotFoundException(Long id) {
        super("Device with id " + id + " not found");
    }
}

