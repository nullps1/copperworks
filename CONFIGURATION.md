# Server balance configuration

Copperworks uses NeoForge `ModConfig.Type.SERVER` and `ModConfigSpec`. With this
project's NeoForge 21.1.251, the generated file is
`<instance>/config/copperworks-server.toml` (development: `run/config/`). A file at
`<world>/serverconfig/copperworks-server.toml` overrides that instance config;
single-player worlds use `saves/<world>/serverconfig/`. NeoForge can also seed a
missing config from `defaultconfigs/copperworks-server.toml`.

**Stop the world/server before editing, then start it again.** Every balance
setting requires a world restart. Runtime file edits are not a supported reload
workflow: existing equipment and active values stay unchanged until restart.
NeoForge synchronizes SERVER config files to connecting clients, not live file
changes to already-connected clients. Editing while running can therefore send
pending file values to new connections; stop/edit/start avoids that mismatch.
Players do not need matching local configuration files. No extra network protocol
or client gameplay config is used.

`/copperworks config` (permission level 2; also usable from the server console)
reports active oxidation settings and each enabled flag plus its four configured
multipliers in fresh/exposed/weathered/oxidized order. Disabled systems always
use 1.0, regardless of the listed configured multipliers.

| Section under `copperworks` | Defaults | Allowed range |
| --- | --- | --- |
| `oxidation.checkInterval` | 600 ticks | 20–72000 |
| `oxidation.baseChance` | 0.0025 | 0–1 |
| `oxidation.wetMultiplier` | 3.0 | 0–100 |
| `durability` | enabled; 1.0 / 1.10 / 1.25 / 1.50 | 0–10 |
| `mining` | enabled; 1.0 / 0.95 / 0.90 / 0.80 | 0–2 |
| `combat` | enabled; 1.0 / 0.97 / 0.93 / 0.85 | 0–2 |
| `armor` | enabled; 1.0 / 0.95 / 0.90 / 0.80 | 0–1 |

Each multiplier section contains `enabled`, `freshMultiplier`, `exposedMultiplier`,
`weatheredMultiplier`, and `oxidizedMultiplier`. A wet chance above 1 always
oxidizes an eligible stack on a check. Waxing only freezes its existing stage.

Armor scales the individual piece's additive defense, retaining its vanilla
modifier ID and equipment slot. Toughness and knockback resistance are unchanged.
Component changes use vanilla equipment comparison/removal/reapplication; no
permanent player modifiers or attribute polling are added.

Armor tooltips and `/copperworks oxidation status` show durability wear and armor
defense. Tools retain durability, mining, and attack information. Displayed
percentages are rounded to whole percentages, as before; gameplay retains the
full configured precision.
