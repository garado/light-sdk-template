
# light-sdk-template

Personal template for developing apps with the Light SDK.

## Information
- Structure
    - Includes a Nix development shell. It is catered toward CLI/headless development and testing on-device.
    - `light-sdk` is a top-level submodule, and custom application code lives in `tool/`.
- Automations
    - **CI:** `:tool:assembleDebug`, `:tool:test` on every push/PR to main
    - **Nightly SDK update check:** Dependabot checks the `light-sdk` submodule daily, opening/updating a PR when there's a new commit.
    - **Template propagation (TODO):** Each child repo will keep `light-sdk-template` as a remote; a script running on my home server will merge template changes in and opens a PR per child repo to reduce manual upkeep.

## Getting started
Requires Github PAT `read:packages` scope for resolving the private Github Packages dependency.
- Local: put it in `local.properties` as `gpr.user`/`gpr.key` (gitignored).
- CI: add it as repo secrets: `GH_PACKAGES_USER`, `GH_PACKAGES_TOKEN` under Settings -> Secrets.

```sh
# Enter Nix development shell
nix develop

# Run interactive project setup script (auto-renames stuff)
./scripts/new-project.sh

# Build
./gradlew :tool:installDebug
```

## SDK summary (for personal reference)
- Components
    - **Layout/Nav:** `LightTopBar`, `LightBottomBar` (up to 5 icons, or 3 if mixing in text), `LightFullscreenModal`, `LightScrollView`, `LightGrid` (spacing constants)
    - **Text:** `LightText` (variants: Title, Subtitle, Heading, Subheading, Copy, Button, Paragraph, ParagraphWide, Detail, Fine, Superfine, Micro), `LightFont`
    - **Input:** `LightTextField` (read-only, opens editor on tap), `LightTextInputEditor`, `LightEmbeddedLp3Keyboard`
    - **Icons/media:** `LightIcon` / `LightIcons` (full icon set), `LightQrCodeScanner`
    - **Interaction/theming:** `lightClickable` (no press ripple), `LightTheme` / `LightThemeColors` / `LightThemeController` / `LightThemeTokens`
- Permissions
    - `INTERNET`
    - `ACCESS_NETWORK_STATE`
    - `WAKE_LOCK`
    - `VIBRATE`
    - `POST_NOTIFICATIONS`
    - `CAMERA`
    - `RECORD_AUDIO`
    - `READ_MEDIA_AUDIO`
    - `ACCESS_FINE_LOCATION`
    - `ACCESS_COARSE_LOCATION`
    - `NFC`
