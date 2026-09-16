# Companion Protocol

## Goal

The phone companion should help control and configure the Wear OS app without requiring complex input on the watch.

## Commands

- Pair
- Ping
- Start remote server
- Stop remote server
- Send file
- Request storage status

## Transport options

Potential transport layers:

1. Wear OS Data Layer API
2. Local HTTP server on the watch
3. Bluetooth/Wi-Fi helper flows exposed by Android APIs

## First implementation direction

Use protocol models first, then choose the transport after the first local build confirms dependencies and Wear OS setup.
