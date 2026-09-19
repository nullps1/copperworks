# Copperworks

Copper tools and armor that age with you. Equipment progresses from **Fresh** to
**Exposed**, **Weathered**, and **Oxidized**, changing its appearance and performance.
Wax a favorite finish or scrape your equipment back toward fresh copper.

## Requirements and installation

- Minecraft **Java Edition 1.21.1**.
- **NeoForge 21.1.251 or newer in the 21.1 series**; 21.1.251 is the tested version.
- **Java 21**.
- Install `copperworks-1.0.0.jar` in the instance's `mods` folder.
- Multiplayer requires Copperworks on **both the server and every client**.
  Use the same mod version. No additional library mods are required.

This build does not target Fabric, Forge, Bedrock, or other Minecraft versions.
For a packwiz-managed pack, update the pack's existing Copperworks artifact and
hash using its established workflow; no packwiz manifest is stored in this repository.

## Equipment

Craft a copper sword, pickaxe, axe, shovel, hoe, helmet, chestplate, leggings, or
boots with copper ingots and the usual equipment crafting shapes. Tools use sticks
for their handles. Copper ingots are also the repair ingredient.

Tools have 180 base durability and an iron-equivalent harvest level, with their
own copper speed and damage values. Fresh armor provides 2 / 5 / 4 / 1 defense
for helmet / chestplate / leggings / boots, with no toughness or knockback resistance.

## Oxidation and care

While carried in either hand or worn, each eligible piece gets an oxidation check
every **600 player ticks** (30 seconds at 20 TPS). Each dry check has a **0.25%**
chance to advance one stage. Being in water, swimming, or standing in rain triples
that chance to **0.75%**. Fully oxidized equipment stops advancing. This is random,
not a fixed countdown. Equipment elsewhere in the inventory or in storage does not age.

To wax equipment, hold it in one hand and **use honeycomb in the other hand**.
Waxing consumes one honeycomb in survival and freezes the current stage.
To remove wax or scrape oxidation, **use an axe in one hand with the equipment in
the other**. The first use removes wax; subsequent successful uses remove one
oxidation stage. Successful axe uses consume durability in survival.

Wax does not restore performance or give an extra bonus: waxed Weathered equipment
still has Weathered penalties. Inventory/held item textures and worn armor textures
follow the individual stack's stage.

## Default balance

| Stage | Durability wear | Mining speed | Attack damage | Armor defense |
| --- | ---: | ---: | ---: | ---: |
| Fresh | 1.00x | 1.00x | 1.00x | 1.00x |
| Exposed | 1.10x | 0.95x | 0.97x | 0.95x |
| Weathered | 1.25x | 0.90x | 0.93x | 0.90x |
| Oxidized | 1.50x | 0.80x | 0.85x | 0.80x |

Wear changes durability consumed, not an item's maximum durability. Fractional wear
is randomized so it averages to the multiplier. Unbreaking and Mending retain their
normal roles; Creative players do not consume durability. Mining penalties apply
after ordinary tool speed effects, including Efficiency and Haste; Creative mining
is unaffected. Sword mining penalties apply only to its naturally efficient blocks.
Attack speed does not change. Armor scales each piece's defense, without affecting
toughness or knockback resistance.

Tooltips show the stage and durability wear, plus mining/attack effects for tools
or defense for armor. Waxed appears only when waxed. Percentages are displayed as
whole numbers; gameplay retains the configured precision.

## Advancements

- **Age of Copper** — obtain Copperworks equipment.
- **It Builds Character** — naturally reach full oxidation on a piece of equipment.
- **Not Getting Any Older** — wax a piece of equipment with honeycomb.

Admin commands change components directly; they do not award the interaction or
natural-oxidation advancements.

## Commands and configuration

All commands require permission level 2 (operator). Equipment commands inspect or
change the item in the player's main hand:

```text
/copperworks oxidation status
/copperworks oxidation set <0–3>
/copperworks oxidation wax
/copperworks oxidation unwax
/copperworks config
```

`status` reports stage, wax state, and current performance effects. `config` is
read-only, works from the server console, and reports active server balance values.

The server generates **`config/copperworks-server.toml`**. An existing file at
`<world>/serverconfig/copperworks-server.toml` overrides it. On multiplayer, NeoForge
sends these settings to clients; players do not manually copy the server config.

**Stop the world/server before editing and restart afterward.** Live edits are not
supported and can leave existing and newly connecting clients with different values.
See [CONFIGURATION.md](CONFIGURATION.md) for keys, ranges, toggles, and synchronization
details. Every penalty system can be independently disabled.

## Compatibility and limitations

Existing vanilla enchantment integration is provided through item tags. Other mods
that replace item attributes, mining events, or equipment rendering may interact
with these systems; compatibility with arbitrary modpacks is not guaranteed.
Resource packs can replace the existing item and armor textures.

This repository currently has **no LICENSE file and no mod icon**. Metadata retains
the existing `All Rights Reserved` value; a release licensing decision and icon are
still pending. No public download/project URL is assumed here.

## Building

With JDK 21 installed, run the checked-in Gradle wrapper:

```powershell
.\gradlew clean build --warning-mode all
```

The output is `build/libs/copperworks-1.0.0.jar`. Gradle, ModDevGradle, NeoForge, and
Java language versions are pinned in the build files. Archives use stable ordering
and omit source timestamps. Rebuilding with the same toolchain and dependencies
should produce the same jar; the release pass compares two clean-build hashes.

Development launches are `./gradlew runClient` and `./gradlew runServer`; server
startup requires accepting Minecraft's EULA in the development run directory.
Generated IDE launch files and output are local, not versioned.

See [CHANGELOG.md](CHANGELOG.md) and [release notes for maintainers](docs/release-readiness.md).
