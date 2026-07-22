
# light-sdk-template

Personal template for developing apps with the Light SDK.

- Includes a Nix development shell. It is catered toward CLI/headless development and testing on-device.
- `light-sdk` is a top-level submodule, and custom application code lives in `tool/`.
- A nightly job runs which checks for any SDK updates and opens a PR (or edits existing) if found. It also builds and run tests (if any) to check compatibility with new SDK updates.
