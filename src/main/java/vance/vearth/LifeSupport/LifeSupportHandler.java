package vance.vearth.LifeSupport;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import vance.vearth.components.ModComponents;
import vance.vearth.item.ModItems;
import vance.vearth.world.dimension.ModDims;

public class LifeSupportHandler {
    private LifeSupportHandler() {}

    private static final int AIR_DRAIN_INTERVAL_TICKS = 2;
    private static final int DAMAGE_INTERVAL_TICKS = 20;
    private static final float FREEZE_DAMAGE = 2.0F;
    private static final float DROWNING_DAMAGE = 2.0F;

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(LifeSupportHandler::onServerTick);
    }

    private static void onServerTick(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            tickPlayer(player);
        }
    }

    private static void tickPlayer(ServerPlayerEntity player) {
        if (player.isSpectator() || player.isCreative()) return;

        boolean onMoon = player.getEntityWorld().getRegistryKey().equals(ModDims.MOON_KEY);
        boolean inOrbit = player.getEntityWorld().getRegistryKey().equals(ModDims.ORBIT_KEY);

        boolean protectedBySuit = isWearingFullArmor(player);

        if (!protectedBySuit && (onMoon || inOrbit)) {
            int age = player.age;
            if (age % AIR_DRAIN_INTERVAL_TICKS == 0) {
                int air = player.getAir();
                if (air > 0) player.setAir(Math.max(-20, air - 25));
            }
            applyFreezeBuildup(player);

            if (player.getAir() <= 0 && age % DAMAGE_INTERVAL_TICKS == 0) {
                player.damage(player.getEntityWorld(), player.getDamageSources().drown(), DROWNING_DAMAGE);
            }
            if (player.getMinFreezeDamageTicks() >= 140 && age % DAMAGE_INTERVAL_TICKS == 0) {
                player.damage(player.getEntityWorld(), player.getDamageSources().freeze(), FREEZE_DAMAGE);
            }

        }


    }

    private static boolean isWearingFullArmor(ServerPlayerEntity player) {
        return is(player.getEquippedStack(EquipmentSlot.HEAD), ModItems.SPACE_SUIT_HELMET)
                && is(player.getEquippedStack(EquipmentSlot.CHEST), ModItems.SPACE_SUIT_CHESTPLATE)
                && player.getEquippedStack(EquipmentSlot.CHEST).getComponents().contains(ModComponents.OXYGEN_STORAGE)
                && hasOxygen(player)
                && is(player.getEquippedStack(EquipmentSlot.LEGS), ModItems.SPACE_SUIT_LEGGINGS)
                && is(player.getEquippedStack(EquipmentSlot.FEET), ModItems.SPACE_SUIT_BOOTS);
    }

    private static boolean is(ItemStack stack, net.minecraft.item.Item item) {
        return !stack.isEmpty() && stack.isOf(item);
    }
    
    private static boolean hasOxygen(ServerPlayerEntity player) {
       if (player.getEquippedStack(EquipmentSlot.CHEST).get(ModComponents.OXYGEN_STORAGE) == null) {
           return false;
       } else return player.getEquippedStack(EquipmentSlot.CHEST).get(ModComponents.OXYGEN_STORAGE) > 0;
    }

    private static void applyFreezeBuildup(ServerPlayerEntity player) {
        int current = player.getFrozenTicks();
        player.setFrozenTicks(Math.min(current + 10, player.getMinFreezeDamageTicks()));
    }



}
