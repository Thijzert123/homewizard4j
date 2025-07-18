---
title: Changelog
layout: default
---

# Changelog

## 2.0.0 (in development)
- `toString()` is no longer overridden in these classes (and subclasses): `Device`, `DeviceState`, `SystemConfiguration`
- `toJson()` has been added in `Device`
- `SERVICE_TYPE` field has been added to `HomeWizardDiscoverer`
- `DeviceState` has been renamed to `EnergySocketState`
- Improve logging in a variety of classes

## 1.0.0
First release. Only API `v1` is supported, and `EnergySocket` and `KWhMeter` are untested.