
# light-sdk-template

Personal template and reference for developing apps with the Light SDK. (WIP)

- Structure
    - Includes a Nix development shell. It is catered toward headless development and on-device testing.
    - `light-sdk` is a top-level submodule, and custom application code lives in `tool/`.
    - Sample tool is updated to my preferred defaults and code style.
- Automations
    - **CI:** `:tool:assembleDebug`, `:tool:test` on every push/PR to main
    - **Nightly SDK update check:** Dependabot checks the `light-sdk` submodule daily, opening/updating a PR when there's a new commit.
    - **Releases:** pushing a `v*` tag (e.g. `v0.2.0`) builds a signed release APK and creates a GitHub Release with it attached.
    - **Template propagation (TODO):** Each child repo will keep `light-sdk-template` as a remote; GH action will check it nightly.

## Getting started

Add a Github personal access token (PAT) with `read:packages` scope for resolving the private Github Packages dependency.

- Local development: Put it in `local.properties` as `gpr.user`/`gpr.key` (gitignored).
- CI: Add it as repo secrets for Actions and Dependabot: `GH_PACKAGES_USER`, `GH_PACKAGES_TOKEN` under Settings -> Secrets.

### Retrofitting existing app
```sh
git checkout -b refactor/light-sdk-rewrite

# Wipe everything!
# rm -rf *
```

```sh
export LIGHT_SDK_TEMPLATE_PATH=~/Github/light/light-sdk-template

# 1. Submodule
git submodule add https://github.com/lightphone/light-sdk.git light-sdk

# 2. Gradle wiring + devshell + gitignore
cp $LIGHT_SDK_TEMPLATE_PATH/settings.gradle.kts .
cp $LIGHT_SDK_TEMPLATE_PATH/build.gradle.kts .
cp $LIGHT_SDK_TEMPLATE_PATH/gradle.properties .
cp -r $LIGHT_SDK_TEMPLATE_PATH/gradle .
cp $LIGHT_SDK_TEMPLATE_PATH/gradlew .
cp $LIGHT_SDK_TEMPLATE_PATH/gradlew.bat .
chmod +x gradlew
cp $LIGHT_SDK_TEMPLATE_PATH/flake.nix .
cp $LIGHT_SDK_TEMPLATE_PATH/.gitignore .

# 3. Demo tool scaffold as a starting point
cp -r $LIGHT_SDK_TEMPLATE_PATH/tool .

# 4. CI + Dependabot + release automation
mkdir -p .github/workflows
cp $LIGHT_SDK_TEMPLATE_PATH/.github/workflows/*.yml .github/workflows/
cp $LIGHT_SDK_TEMPLATE_PATH/.github/dependabot.yml .github/

# 5. Helper script + license
mkdir -p scripts
cp $LIGHT_SDK_TEMPLATE_PATH/scripts/new-project.sh scripts/
chmod +x scripts/new-project.sh
cp $LIGHT_SDK_TEMPLATE_PATH/LICENSE .
```

### Pulling in light-sdk-template updates
For updating the flake, custom scripts, or custom gradle wiring (not light-sdk).
```sh
# One-time setup
git remote add template git@github.com:garado/light-sdk-template.git

# Pull in template improvements
git fetch template
git merge template/main --allow-unrelated-histories

# This will conflict on files meant to stay project-specific
# TODO Add script to autoresolve
```

### Setup
```sh
# Enter Nix development shell
nix develop

# Run interactive project setup script (auto-renames stuff)
./scripts/new-project.sh

# Build
./gradlew :tool:installDebug
```

## SDK summary
For personal reference
- Components
    - **Layout/Nav:**
        - `LightTopBar`
        - `LightBottomBar` (up to 5 icons, or 3 if mixing in text)
        - `LightFullscreenModal`
        - `LightScrollView`
        - `LightGrid` (spacing constants)
        - `LightModalManager`: transient overlays, timeout/dismiss
    - **Text:**
        - `LightText` (variants: Title, Subtitle, Heading, Subheading, Copy, Button, Paragraph, ParagraphWide, Detail, Fine, Superfine, Micro)
        - `LightFont`
    - **Input:**
        - `LightTextField` (read-only, opens editor on tap)
        - `LightTextInputEditor`
        - `LightEmbeddedLp3Keyboard`
        - `LightKeyHandler`: hardware key event forwarding (down/up/multiple)
    - **Icons/media:**
        - `LightIcon` / `LightIcons` (full icon set)
        - `LightQrCodeScanner`
        - `LightProgressBar`
    - **Audio:**
        - `LightAudio` (factory off `SealedLightActivity`): handles foreground audio focus internally
        - `LightAudioPlayer`
        - `LightAudioRecorder`
        - `LightAudioCapture`
        - `LightAudioVoice`
    - **Interaction/theming:**
        - `lightClickable` (no press ripple)
        - `LightTheme` / `LightThemeColors` / `LightThemeController` / `LightThemeTokens`
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
