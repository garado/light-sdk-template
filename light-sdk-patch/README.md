
# light-sdk-patch

Patchfiles in this directory are applied to `light-sdk` at build time, ensuring traceability for any SDK edits.

## Creating/updating a patch

```sh
# Edit the file directly inside the submodule
nvim light-sdk/path/to/File.kt

# Generate diff
git -C light-sdk diff -- <relative/path/to/file> > light-sdk-patch/<name>.patch

# Restore the original submodule state
git -C light-sdk checkout -- <relative/path/to/file>
```

## Patches

None yet! (This is where you'd describe what each patch is and why it's made.)
