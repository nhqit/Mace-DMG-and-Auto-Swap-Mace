package com.iamnhq.macedmg;

import java.lang.reflect.Constructor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;

@EventBusSubscriber(modid = MaceDmgMod.MOD_ID, value = Dist.CLIENT)
public class MaceDmgAttackHandler {
    private static final int HOTBAR_SIZE = 9;
    private static final double BOOST_Y_OFFSET = Math.sqrt(500.0);

    // Tag for maces: includes minecraft:mace by default; add other mod maces via datapack.
    private static final TagKey<Item> MACES_TAG = ItemTags.create(
            ResourceLocation.fromNamespaceAndPath(MaceDmgMod.MOD_ID, "maces"));

    private static boolean isMace(ItemStack stack) {
        return stack.getItem() instanceof MaceItem || stack.is(MACES_TAG);
    }

    private static int lastTriggerTick = -1;
    private static int pendingAttackTargetId = -1;
    private static int attackAtTick = -1;
    private static int pendingSwapBackSlot = -1;
    private static int swapBackAtTick = -1;
    private static boolean isPerformingScheduledAttack = false;
    private static Constructor<?> posCtorWithCollision;
    private static boolean checkedPosCtorWithCollision;

    private static void clearPendingState() {
        pendingAttackTargetId = -1;
        attackAtTick = -1;
        pendingSwapBackSlot = -1;
        swapBackAtTick = -1;
    }

    @SubscribeEvent
    public static void onAttackEntity(InputEvent.InteractionKeyMappingTriggered event) {
        if (!MaceDmgMod.enabled) return;
        if (isPerformingScheduledAttack) return;
        if (!event.isAttack()) return;
        if (pendingAttackTargetId >= 0 || pendingSwapBackSlot >= 0) return;

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null || mc.getConnection() == null) return;
        if (mc.gameMode == null) return;

        // Only run when this click is actually targeting an entity hit.
        if (mc.hitResult == null || mc.hitResult.getType() != HitResult.Type.ENTITY) return;
        if (!(mc.hitResult instanceof EntityHitResult entityHitResult)) return;
        if (!(entityHitResult.getEntity() instanceof LivingEntity living) || !living.isAlive()) return;

        // Guard against duplicate event fires in the same tick.
        if (player.tickCount == lastTriggerTick) return;
        lastTriggerTick = player.tickCount;

        ItemStack mainHand = player.getMainHandItem();
        boolean holdingMace = isMace(mainHand);
        boolean holdingWeapon = mainHand.getItem() instanceof SwordItem || mainHand.getItem() instanceof AxeItem;

        int maceSlot = -1;
        if (!holdingMace && holdingWeapon) {
            for (int i = 0; i < HOTBAR_SIZE; i++) {
                if (isMace(player.getInventory().getItem(i))) {
                    maceSlot = i;
                    break;
                }
            }
        }

        if (holdingMace) {
            doFakeFall(mc, player);
            // Let original attack proceed normally
        } else if (maceSlot != -1) {
            event.setCanceled(true);
            event.setSwingHand(false);

            int prevSlot = player.getInventory().selected;
            if (prevSlot == maceSlot) return;

            // 1. Swap to Mace before vanilla attack packet is sent
            player.getInventory().selected = maceSlot;
            mc.getConnection().send(new ServerboundSetCarriedItemPacket(maceSlot));

            // 2. Attack next tick to avoid re-entrant packet flow in the same callback.
            pendingAttackTargetId = entityHitResult.getEntity().getId();
            attackAtTick = player.tickCount + 1;

            // 3. Swap back after scheduled attack.
            pendingSwapBackSlot = prevSlot;
            swapBackAtTick = player.tickCount + 2;
        }
    }

    @SubscribeEvent
    public static void onClientTickPost(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (!MaceDmgMod.enabled || player == null || mc.getConnection() == null) {
            clearPendingState();
            return;
        }

        if (pendingAttackTargetId >= 0 && player.tickCount >= attackAtTick) {
            int targetId = pendingAttackTargetId;
            pendingAttackTargetId = -1;
            attackAtTick = -1;

            if (mc.level != null && mc.gameMode != null) {
                var target = mc.level.getEntity(targetId);
                if (target instanceof LivingEntity living && living.isAlive()) {
                    isPerformingScheduledAttack = true;
                    try {
                        // Wurst-like timing: fake fall immediately before the actual hit packet.
                        doFakeFall(mc, player);

                        mc.gameMode.attack(player, target);
                        player.swing(InteractionHand.MAIN_HAND);
                    } finally {
                        isPerformingScheduledAttack = false;
                    }
                }
            }
        }

        // Safety timeout to avoid getting stuck in pending states.
        if (pendingAttackTargetId >= 0 && attackAtTick >= 0 && player.tickCount > attackAtTick + 3) {
            pendingAttackTargetId = -1;
            attackAtTick = -1;
        }

        if (pendingSwapBackSlot < 0) return;


        if (swapBackAtTick >= 0 && player.tickCount > swapBackAtTick + 5) {
            pendingSwapBackSlot = -1;
            swapBackAtTick = -1;
            return;
        }

        if (player.tickCount < swapBackAtTick) return;

        int slot = pendingSwapBackSlot;
        pendingSwapBackSlot = -1;
        swapBackAtTick = -1;

        if (slot < 0 || slot >= HOTBAR_SIZE) return;
        if (player.getInventory().selected == slot) return;

        player.getInventory().selected = slot;
        mc.getConnection().send(new ServerboundSetCarriedItemPacket(slot));
    }

    private static void doFakeFall(Minecraft mc, LocalPlayer player) {
        // Match Wurst MaceDMG packet pattern: 4x0, sqrt(500), 0.
        for (int i = 0; i < 4; i++) {
            sendFakeY(mc, player, 0.0);
        }
        sendFakeY(mc, player, BOOST_Y_OFFSET); // ~22.36 blocks "above" -> large fall bonus
        sendFakeY(mc, player, 0.0);            // "land" at player Y
    }

    private static void sendFakeY(Minecraft mc, LocalPlayer player, double yOffset) {
        if (!checkedPosCtorWithCollision) {
            checkedPosCtorWithCollision = true;
            try {
                posCtorWithCollision = ServerboundMovePlayerPacket.Pos.class.getConstructor(
                        double.class, double.class, double.class, boolean.class, boolean.class);
            } catch (ReflectiveOperationException ignored) {
                posCtorWithCollision = null;
            }
        }

        if (posCtorWithCollision != null) {
            try {
                Object packet = posCtorWithCollision.newInstance(
                        player.getX(),
                        player.getY() + yOffset,
                        player.getZ(),
                        false,
                        player.horizontalCollision
                );
                mc.getConnection().send((ServerboundMovePlayerPacket) packet);
                return;
            } catch (ReflectiveOperationException ignored) {
                posCtorWithCollision = null;
            }
        }

        mc.getConnection().send(new ServerboundMovePlayerPacket.Pos(
                player.getX(),
                player.getY() + yOffset,
                player.getZ(),
                false
        ));
    }
}
