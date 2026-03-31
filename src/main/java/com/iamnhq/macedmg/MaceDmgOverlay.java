package com.iamnhq.macedmg;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = MaceDmgMod.MOD_ID, value = Dist.CLIENT)
public class MaceDmgOverlay {
    private static final Component OVERLAY_ON = Component.literal("\u00a7a[MaceDMGSwap] ON");
    private static final Component OVERLAY_OFF = Component.literal("\u00a7c[MaceDMGSwap] OFF");

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) return;

        GuiGraphics graphics = event.getGuiGraphics();
        int screenWidth = mc.getWindow().getGuiScaledWidth();

        Component text = MaceDmgMod.enabled ? OVERLAY_ON : OVERLAY_OFF;

        // Draw in top-right corner
        int textWidth = mc.font.width(text);
        graphics.drawString(mc.font,
            text,
            screenWidth - textWidth - 4,
            4,
            0xFFFFFF
        );
    }
}
