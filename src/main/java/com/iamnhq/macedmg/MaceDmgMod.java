package com.iamnhq.macedmg;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(MaceDmgMod.MOD_ID)
public class MaceDmgMod {

    public static final String MOD_ID = "macedmg";
    public static final Logger LOGGER = LogUtils.getLogger();

    /** Whether the MaceDMG feature is currently active */
    public static volatile boolean enabled = false;

    public MaceDmgMod(IEventBus modEventBus, ModContainer modContainer) {
        // Register client-side event listeners on the NeoForge event bus
        NeoForge.EVENT_BUS.register(MaceDmgKeyHandler.class);
        NeoForge.EVENT_BUS.register(MaceDmgAttackHandler.class);
        NeoForge.EVENT_BUS.register(MaceDmgOverlay.class);
        LOGGER.info("[MaceDMG] Mod loaded. Press V to toggle.");
    }
}
