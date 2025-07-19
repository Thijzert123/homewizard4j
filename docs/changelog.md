---
title: Changelog
layout: default
---

# Changelog

## 2.0.0 (in development)
This new release offers new features, as well as some breaking changes to make the API more clear.

### Breaking changes
- `DeviceState` has been renamed to `EnergySocketState`
- `toString()` is no longer overridden in these classes (and subclasses): `Device`, `DeviceState`, `SystemConfiguration`

### Features added to `HomeWizardDiscoverer` (non-breaking)
`HomeWizardDiscoverer` adds tools so you don't have to write as much code.
For some of these tools to work, an enum `DeviceType` has been added as an embedded enum in `HomeWizardDiscoverer`.

These methods have been added:
- `waitForMillis(long)`: blocks current thread for a specified amount of time in millis
- `waitForDevices(DeviceType, int)`: block current thread until a specified amount of devices are found
- `HomeWizardDiscoverer(long)`: new constructor that blocks for specified amount of time in millis and
   and closes the discoverer after
- `getDevices(DeviceType)`: returns a list of devices with the specified type of device

### Miscellaneous changes (non-breaking)
- `toJson()` has been added in `Device`. It converts an instance to a JSON that you can use for saving the device to a file.
- `SERVICE_TYPE` field has been added to `HomeWizardDiscoverer`
- Improve logging in a variety of classes

## 1.0.0
First release. Only API `v1` is supported, and `EnergySocket` and `KWhMeter` are untested.