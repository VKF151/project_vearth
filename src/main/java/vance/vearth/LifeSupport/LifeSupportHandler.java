package vance.vearth.LifeSupport;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
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
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            tickPlayer(player);
        }
    }

    private static void tickPlayer(ServerPlayer player) {
        if (player.isSpectator() || player.isCreative()) return;

        boolean onMoon = player.level().dimension().equals(ModDims.MOON_KEY);
        boolean inOrbit = player.level().dimension().equals(ModDims.ORBIT_KEY);

        boolean protectedBySuit = isWearingFullArmor(player);

        if (!protectedBySuit && (onMoon || inOrbit)) {
            int age = player.tickCount;
            if (age % AIR_DRAIN_INTERVAL_TICKS == 0) {
                int air = player.getAirSupply();
                if (air > 0) player.setAirSupply(Math.max(-20, air - 25));
            }
            applyFreezeBuildup(player);

            if (player.getAirSupply() <= 0 && age % DAMAGE_INTERVAL_TICKS == 0) {
                player.hurtServer(player.level(), player.damageSources().drown(), DROWNING_DAMAGE);
            }
            if (player.getTicksRequiredToFreeze() >= 140 && age % DAMAGE_INTERVAL_TICKS == 0) {
                player.hurtServer(player.level(), player.damageSources().freeze(), FREEZE_DAMAGE);
            }

        }


    }

    private static boolean isWearingFullArmor(ServerPlayer player) {
        return is(player.getItemBySlot(EquipmentSlot.HEAD), ModItems.SPACE_SUIT_HELMET)
                && is(player.getItemBySlot(EquipmentSlot.CHEST), ModItems.SPACE_SUIT_CHESTPLATE)
                && player.getItemBySlot(EquipmentSlot.CHEST).getComponents().has(ModComponents.OXYGEN_STORAGE)
                && hasOxygen(player)
                && is(player.getItemBySlot(EquipmentSlot.LEGS), ModItems.SPACE_SUIT_LEGGINGS)
                && is(player.getItemBySlot(EquipmentSlot.FEET), ModItems.SPACE_SUIT_BOOTS);
    }

    private static boolean is(ItemStack stack, net.minecraft.world.item.Item item) {
        return !stack.isEmpty() && stack.is(item);
    }
    
    private static boolean hasOxygen(ServerPlayer player) {
       if (player.getItemBySlot(EquipmentSlot.CHEST).get(ModComponents.OXYGEN_STORAGE) == null) {
           return false;
       } else return player.getItemBySlot(EquipmentSlot.CHEST).get(ModComponents.OXYGEN_STORAGE) > 0;
    }

    private static void applyFreezeBuildup(ServerPlayer player) {
        int current = player.getTicksFrozen();
        player.setTicksFrozen(Math.min(current + 10, player.getTicksRequiredToFreeze()));
    }



}
