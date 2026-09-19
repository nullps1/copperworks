# Release maintenance notes

## Scope

v1.0.0 targets Minecraft 1.21.1, NeoForge 21.1.251+, within the 21.1 line, and Java 21.
Do not publish this jar as supporting other Minecraft versions merely because their
version numbers are close. Keep registry, component, recipe, advancement, config,
model, and texture IDs stable for existing saves and resource packs.

## API review

The deprecated `ItemPropertyFunction` is deliberately retained, without suppressing
its warning. The 1.21.1 `ClampedItemPropertyFunction.call` clamps output to 0–1;
the existing `copperworks:oxidation` model contract uses integer stages 0–3.
Replacing it would require changing every threshold or overriding the deprecated
method anyway. See [oxidation texture implementation](oxidation-textures.md).

Client registration remains isolated by `@Mod(..., dist = Dist.CLIENT)` and runs
through enqueued client setup. Common gameplay code does not import client classes.
Per-stack attributes use `ItemAttributeModifierEvent`; armor preserves the existing
slot-specific vanilla modifier IDs. Durability uses NeoForge's item damage hook
before Unbreaking. No new event loops or persistent player modifiers are needed.
SERVER config uses `ModConfigSpec`, native synchronization, and restart-required
values. See [configuration lifecycle and limitations](../CONFIGURATION.md).

## Release checks

1. Run `./gradlew clean build --warning-mode all` with JDK 21.
2. Start the dedicated server and client; check resource loading, config, recipes,
   tags, and advancements. Exercise oxidation, wax/scrape, performance effects,
   equipment changes, enchantments, commands, and permission checks.
3. Exercise default, disabled, and custom server values. Stop the server before
   config edits. Confirm connecting clients use server values.
4. Inspect `build/libs/copperworks-1.0.0.jar` for expanded metadata and absence of
   harnesses, logs, runtime configs, generated IDE files, or development resources.
5. Compare SHA-256 after two clean builds with the same toolchain. Record the final
   jar's byte size and SHA-256 with the published release.

Generated `bin/`, `tempjar/`, IDE launches, and runtime directories are not sources
and must not be committed or packaged. Preserve the Gradle wrapper jar.

## Publication decisions still needed

- No LICENSE file exists. Preserve the existing metadata value until the owner
  chooses the release license; do not infer one from dependencies or public hosting.
- No mod icon is configured. Supply approved artwork and then set its metadata path.
- No packwiz manifests or public Modrinth project metadata live in this repository.
  Verify the external distribution workflow and its artifact hash before publishing.
- A build or draft PR does not publish a Modrinth release. Review validation results,
  merge the release changes, and publish the reviewed artifact through the owner's
  release process.
