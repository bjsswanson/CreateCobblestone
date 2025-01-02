package net.createcobblestone.index.fabric;

import com.simibubi.create.foundation.config.ConfigBase;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.api.fml.event.config.ModConfigEvent;
import net.createcobblestone.CreateCobblestoneMod;
import net.createcobblestone.config.CreateCobblestoneCommon;
import net.createcobblestone.index.Config;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.function.Supplier;

public class ConfigImpl extends Config {
    private static <T extends ConfigBase> T register(Supplier<T> factory, ModConfig.Type side) {
        Pair<T, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(builder -> {
            T config = factory.get();
            config.registerAll(builder);
            return config;
        });

        T config = specPair.getLeft();
        config.specification = specPair.getRight();
        CONFIGS.put(side, config);
        return config;
    }

    public static void register() {
        common = register(CreateCobblestoneCommon::new, ModConfig.Type.COMMON);

        for (Map.Entry<ModConfig.Type, ConfigBase> pair : CONFIGS.entrySet())
            ModLoadingContext.registerConfig(CreateCobblestoneMod.MOD_ID, pair.getKey(), pair.getValue().specification);

        ModConfigEvent.LOADING.register(net.createcobblestone.index.Config::onLoad);
        ModConfigEvent.RELOADING.register(net.createcobblestone.index.Config::onReload);
    }
}
