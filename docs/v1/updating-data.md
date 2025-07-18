---
title: Updating data
parent: V1
nav_order: 4
layout: default
---

# Updating data
You might have already noticed that most getters of a `Device` return an `Optional` of some kind. This has two reasons:
- For some fields, you first have to update the device by calling one of the `update*()` methods. In the Javadocs you can see what update method you have to call for a specific field to update.
- Not all data points are returned by the official API when updating. When you don't use gas, the P1 meter won't return data points that are about gas.

The default value for all fields is `Optional.empty()` (or with another form of `Optional`, like `OptionalInt` or `OptionalDouble`), except for some. These fields are required when initializing the class, so you can always access them. Some of these values are never able to change, for example, host address and port.