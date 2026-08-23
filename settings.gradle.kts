pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven {
            name = "JitPack"
            url = uri("https://jitpack.io")
        }
    }
    versionCatalogs {
        create("libs") {
            from(files("light-sdk/gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "light-sdk-template"

// Apply local light-sdk-patch/*.patch to the light-sdk submodule before its build
// files/sources are read below. This is reverted in buildFinished to keep the submodule clean.
val sdkPatchesDir = file("light-sdk-patch")
val sdkDir = file("light-sdk")
if (sdkPatchesDir.exists()) {
    val patchFiles = sdkPatchesDir.listFiles { f -> f.isFile && f.extension == "patch" }
        ?.sortedBy { it.name }
        .orEmpty()

    fun runGit(vararg args: String): Int = ProcessBuilder("git", *args)
        .directory(sdkDir)
        .redirectErrorStream(true)
        .start()
        .waitFor()

    val appliedPaths = mutableListOf<String>()
    for (patchFile in patchFiles) {
        // paths this patch touches, so we know what to restore afterward
        val paths = patchFile.readLines()
            .filter { it.startsWith("+++ b/") }
            .map { it.removePrefix("+++ b/") }
        val exitCode = runGit("apply", patchFile.absolutePath)
        check(exitCode == 0) {
            "Failed to apply $patchFile - light-sdk submodule has likely drifted from what this patch expects. " +
                "Regenerate it against the current submodule contents."
        }
        appliedPaths += paths
    }

    if (appliedPaths.isNotEmpty()) {
        gradle.buildFinished {
            for (relativePath in appliedPaths) {
                // tracked files: restore to their committed contents
                runGit("checkout", "--", relativePath)
                // untracked/new files: remove them entirely
                runGit("clean", "-f", "--", relativePath)
            }
        }
    }
}

includeBuild("light-sdk/plugin")

include(":lint-rules")
project(":lint-rules").projectDir = file("light-sdk/lint-rules")

include(":sdk:shared")
project(":sdk:shared").projectDir = file("light-sdk/sdk/shared")

include(":sdk:ui")
project(":sdk:ui").projectDir = file("light-sdk/sdk/ui")

include(":sdk:client")
project(":sdk:client").projectDir = file("light-sdk/sdk/client")

include(":sdk:server")
project(":sdk:server").projectDir = file("light-sdk/sdk/server")

include(":sdk:emulator")
project(":sdk:emulator").projectDir = file("light-sdk/sdk/emulator")

project(":sdk").projectDir = file("light-sdk/sdk")

include(":tool")
