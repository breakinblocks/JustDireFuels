package com.breakinblocks.justdirefuels;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(JustDireFuels.MOD_ID)
public class JustDireFuels {
    public static final String MOD_ID = "justdirefuels";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public JustDireFuels(IEventBus eventBus, ModContainer container, Dist dist) {
        LOGGER.info("Just Dire Fuels loading");
    }
}
