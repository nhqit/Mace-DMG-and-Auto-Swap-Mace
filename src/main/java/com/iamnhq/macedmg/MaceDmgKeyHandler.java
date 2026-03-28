package com.iamnhq.macedmg;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = MaceDmgMod.MOD_ID, value = Dist.CLIENT)
public class MaceDmgKeyHandler {

    /** GLFW key code for the toggle key – V */
    private static final int TOGGLE_KEY = GLFW.GLFW_KEY_V;
    private static boolean wasPressed = false;

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();

        // Only handle when in-game (screen is null = no GUI open)
        if (mc.screen != null) return;

        boolean pressed = event.getKey() == TOGGLE_KEY
                && event.getAction() == GLFW.GLFW_PRESS;

        if (pressed && !wasPressed) {
            MaceDmgMod.enabled = !MaceDmgMod.enabled;
            MaceDmgMod.LOGGER.info("[MaceDMG] {}",
                    MaceDmgMod.enabled ? "ENABLED" : "DISABLED");
        }
        wasPressed = pressed;
    }
}
