package com.nullps1.copperworks;

import java.util.List;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public final class CopperworksConfig {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.IntValue CHECK_INTERVAL;
    public static final ModConfigSpec.DoubleValue BASE_CHANCE;
    public static final ModConfigSpec.DoubleValue WET_MULTIPLIER;
    public static final Multipliers DURABILITY;
    public static final Multipliers MINING;
    public static final Multipliers COMBAT;
    public static final Multipliers ARMOR;

    static {
        var builder = new ModConfigSpec.Builder();
        builder.comment("Server-authoritative balance. Stop the world/server before editing; restart to apply.").push("copperworks");
        builder.push("oxidation");
        CHECK_INTERVAL = builder.comment("Ticks between oxidation checks for held and worn copper equipment.")
            .worldRestart().defineInRange("checkInterval", 600, 20, 72000);
        BASE_CHANCE = builder.comment("Dry oxidation probability per eligible stack per check.")
            .worldRestart().defineInRange("baseChance", 0.0025D, 0.0D, 1.0D);
        WET_MULTIPLIER = builder.comment("Oxidation chance multiplier in water or rain; chances above 1 always succeed.")
            .worldRestart().defineInRange("wetMultiplier", 3.0D, 0.0D, 100.0D);
        builder.pop();
        DURABILITY = new Multipliers(builder, "durability", "Durability wear before Unbreaking", 10.0D, 1.0D, 1.10D, 1.25D, 1.50D);
        MINING = new Multipliers(builder, "mining", "Effective tool mining speed", 2.0D, 1.0D, 0.95D, 0.90D, 0.80D);
        COMBAT = new Multipliers(builder, "combat", "Main-hand tool attack damage", 2.0D, 1.0D, 0.97D, 0.93D, 0.85D);
        ARMOR = new Multipliers(builder, "armor", "Each armor piece's defense (not toughness or knockback resistance)", 1.0D, 1.0D, 0.95D, 0.90D, 0.80D);
        builder.pop();
        SPEC = builder.build();
    }

    private CopperworksConfig() {
    }

    // Defaults are only used outside a loaded world, e.g. the title-screen creative preview.
    public static <T> T value(ModConfigSpec.ConfigValue<T> value) {
        return SPEC.isLoaded() ? value.get() : value.getDefault();
    }

    public static void onConfigLoading(ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == SPEC) {
            cacheActiveValues();
        }
    }

    public static void onConfigReloading(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == SPEC && ServerLifecycleHooks.getCurrentServer() == null) {
            // NeoForge delivers remote SERVER configs as Reloading; there is no local logical server.
            // Discard any preview/previous-world cache so the received server values win.
            cacheActiveValues();
        }
        // File watcher reloads deliberately keep the active worldRestart values until the next world.
    }

    private static void cacheActiveValues() {
        prime(CHECK_INTERVAL);
        prime(BASE_CHANCE);
        prime(WET_MULTIPLIER);
        for (var group : List.of(DURABILITY, MINING, COMBAT, ARMOR)) {
            prime(group.enabled);
            group.stages.forEach(CopperworksConfig::prime);
        }
    }

    private static void prime(ModConfigSpec.ConfigValue<?> value) {
        value.clearCache();
        value.get();
    }

    public static final class Multipliers {
        public final ModConfigSpec.BooleanValue enabled;
        public final List<ModConfigSpec.DoubleValue> stages;

        private Multipliers(ModConfigSpec.Builder builder, String section, String description, double max,
                            double fresh, double exposed, double weathered, double oxidized) {
            builder.comment(description + ". Waxing preserves the stage and does not change these multipliers.").push(section);
            enabled = builder.comment("Enable scaling; false uses 1.0 for every stage.").worldRestart().define("enabled", true);
            stages = List.of(
                define(builder, "freshMultiplier", fresh, max),
                define(builder, "exposedMultiplier", exposed, max),
                define(builder, "weatheredMultiplier", weathered, max),
                define(builder, "oxidizedMultiplier", oxidized, max)
            );
            builder.pop();
        }

        private static ModConfigSpec.DoubleValue define(ModConfigSpec.Builder builder, String name, double fallback, double max) {
            return builder.comment("Multiplier for this oxidation stage; 1.0 is unchanged.")
                .worldRestart().defineInRange(name, fallback, 0.0D, max);
        }

        public double multiplier(int stage) {
            return value(enabled) ? value(stages.get(stage)) : 1.0D;
        }
    }
}
