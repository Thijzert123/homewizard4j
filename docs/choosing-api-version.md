---
title: Choosing API version
nav_order: 2
layout: default
---

# Choosing API version
HomeWizard has two API versions: `v1` and `v2`. The official `v1` API is going to be phased out and will probably not
support new HomeWizard devices in the future. The `v2` API is newer and supports new devices, for example,
the [Plug-In Battery](https://www.homewizard.com/plug-in-battery). This version only allows access via HTTPS,
whereas `v1` only supported HTTP. Support for `v2` in `homewizard4j` is still in development. `v1` is fully supported.

As mentioned before, `v2` is newer. Some HomeWizard devices are not yet supported for the `v2` API:

| Device          | v1  | v2                           |
|:----------------|:----|:-----------------------------|
| P1 meter        | Yes | In beta                      |
| Energy socket   | Yes | In development by HomeWizard |
| Water meter*    | Yes | In development by HomeWizard |
| kWh meter       | Yes | In beta                      |
| Energy display  | No  | In development by HomeWizard |
| Plug-in battery | No  | Yes                          |

_* water meter only supports the API if it is connected via USB_