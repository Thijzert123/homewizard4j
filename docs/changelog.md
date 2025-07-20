---
title: Changelog
layout: default
---

# Changelog

## 2.1.0 (in development)
### New features
- `HomeWizardDiscoverer(DeviceType, int)`: new constructor that blocks until a specific number of devices with
 specified type are discovered and closes the discoverer afterward
- `thenClose()` has been added to `HomeWizardDiscoverer`: it closes the discoverer,
 then returns the current discoverer instance

### Bug fixes
- When merging `HomeWizardDiscoverer`, it no longer discovers the same device twice

## 2.0.0
This new release offers new features, as well as some breaking changes to make the API more clear.

### Breaking changes
- `DeviceState` has been renamed to `EnergySocketState`
- `toString()` is no longer overridden in these classes (and subclasses): `Device`, `DeviceState`, `SystemConfiguration`

### Features added to `HomeWizardDiscoverer` (non-breaking)
`HomeWizardDiscoverer` adds tools so you don't have to write as much code.
For some of these tools to work, an enum `DeviceType` has been added as an embedded enum in `HomeWizardDiscoverer`.

These methods have been added:
- `waitForMillis(long)`: blocks current thread for a specified amount of time in millis
- `waitForDevices(DeviceType, int)`: block current thread until a specified amount of devices is found
- `HomeWizardDiscoverer(long)`: new constructor that blocks for specified amount of time in millis
   and closes the discoverer after
- `getDevices(DeviceType)`: returns a list of devices with the specified type of device

### JSON tools for `Device` (non-breaking)
Tools have been added to `Device` that allow you to (de)serialize JSONs to `Device` instances:
- `toJson()`: converts an instance to a JSON that you can use for saving the device to a file.
- `updateFromJson(String)`: uses a provided JSON to update all the data in a `Device`.

### Miscellaneous changes (non-breaking)
- `SERVICE_TYPE` field has been added to `HomeWizardDiscoverer`
- Improved logging in a variety of classes

## 1.0.0
First release. Only API `v1` is supported, and `EnergySocket` and `KWhMeter` are untested.