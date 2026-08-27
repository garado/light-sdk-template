
# light-sdk-template

An opinionated template for developing apps with the Light SDK.

- Structure
    - `light-sdk` is a top-level submodule, and custom application code lives in `tool/`. This template provides custom Gradle wiring to get this architecture to work correctly.
        - This is to get around the one-fork-per-account problem while also cleanly ensuring tools can easily pull SDK updates.
    - **Patches:** includes an `sdk-patch` dir for traceability to any edits to the upstream SDK. These are automatically applied at build time.
    - Includes a sample tool with my preferred defaults and code style.
    - Includes a Nix development shell. It is catered toward headless development and on-device testing.
- Automations
    - **CI:** `:tool:assembleDebug`, `:tool:test` on every push/PR to main
    - **Nightly SDK update check:** Dependabot checks the `light-sdk` submodule daily, opening/updating a PR when there's a new commit.
    - **Releases:** pushing a `v*` tag (e.g. `v0.2.0`) builds a signed release APK and creates a GitHub Release with it attached.
    - **Template propagation (TODO):** Each child repo will keep `light-sdk-template` as a remote; GH action will check it nightly.

## Getting started

### Starting fresh

First press 'Use this template' -> 'Create a new repository'.

```sh
# Clone your new repo locally, cd into it, and then:
git submodule update --init --recursive

# run interactive project setup script (auto-renames stuff)
./scripts/new-project.sh

# build
./gradlew :tool:installDebug
```

### Retrofitting existing app

I have a lot of old apps written with Vandam's [light-template](https://github.com/vandamd/light-template). This is my process for retrofitting those repos to use this structure.

First make a new branch for the rewrite:

```sh
git checkout -b refactor/light-sdk-rewrite

# Wipe everything!
# rm -rf *
```

Clone `light-sdk-template` somewhere, then set this env var accordingly:

```sh
export LIGHT_SDK_TEMPLATE_PATH=~/path/to/your/light-sdk-template
```

Then `cd` to the repo to be retrofitted, and copy+paste this block of commands:

```sh
# 1. Track light-sdk
git submodule add https://github.com/lightphone/light-sdk.git light-sdk

# 2. Copy gradle wiring + devshell + patches + gitignore
cp $LIGHT_SDK_TEMPLATE_PATH/settings.gradle.kts .
cp $LIGHT_SDK_TEMPLATE_PATH/build.gradle.kts .
cp $LIGHT_SDK_TEMPLATE_PATH/gradle.properties .
cp -r $LIGHT_SDK_TEMPLATE_PATH/gradle .
cp $LIGHT_SDK_TEMPLATE_PATH/gradlew .
cp $LIGHT_SDK_TEMPLATE_PATH/gradlew.bat .
chmod +x gradlew
cp -r $LIGHT_SDK_TEMPLATE_PATH/light-sdk-patch .
cp $LIGHT_SDK_TEMPLATE_PATH/flake.nix .
cp $LIGHT_SDK_TEMPLATE_PATH/.gitignore .

# 3. Copy demo tool as a starting point
cp -r $LIGHT_SDK_TEMPLATE_PATH/tool .

# 4. Copy CI + Dependabot + release automation
mkdir -p .github/workflows
cp $LIGHT_SDK_TEMPLATE_PATH/.github/workflows/*.yml .github/workflows/
cp $LIGHT_SDK_TEMPLATE_PATH/.github/dependabot.yml .github/

# 5. Copy helper script + license
mkdir -p scripts
cp $LIGHT_SDK_TEMPLATE_PATH/scripts/new-project.sh scripts/
chmod +x scripts/new-project.sh
cp $LIGHT_SDK_TEMPLATE_PATH/LICENSE .
```

### Pulling in light-sdk-template updates
There is no automatic way to pull changes from the template into your repo after you've already made the repo. This is my process for it.

```sh
# One-time setup
git remote add template git@github.com:garado/light-sdk-template.git

# Pull in template improvements
git fetch template
git merge template/main --allow-unrelated-histories

# This will conflict on files meant to stay project-specific
# TODO Add script to autoresolve
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
