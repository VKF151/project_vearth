package vance.vearth.gravity;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import vance.vearth.world.dimension.ModDims;

public class GravityHandler {

    private GravityHandler() {}

    public static void register() {
        ServerTickEvents.END_WORLD_TICK.register(GravityHandler::onWorldTick);
    }

    private static void onServerTick(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            updatePlayerGravity(player);
        }
    }
    private static void onWorldTick(ServerWorld world) {
        final boolean onMoon  = world.getRegistryKey().equals(ModDims.MOON_KEY);
        final boolean inOrbit = world.getRegistryKey().equals(ModDims.ORBIT_KEY);

        final double gravityTarget = inOrbit ? GravityModifiers.ORBIT_GRAV : (onMoon ? GravityModifiers.MOON_GRAV : GravityModifiers.EARTH_GRAV);

        final  double ratio = (gravityTarget / GravityModifiers.EARTH_GRAV);
        final double safeFallTarget = (ratio <= 0.0D) ? 1_000_000.0D : (3.0D / ratio);

        for (Entity e : world.iterateEntities()) {
            if (!(e instanceof LivingEntity living)) continue;

            EntityAttributeInstance grav = living.getAttributeInstance(EntityAttributes.GRAVITY);
            EntityAttributeInstance safeFall = living.getAttributeInstance(EntityAttributes.SAFE_FALL_DISTANCE);
            if (grav == null || safeFall == null) continue;

            if (grav.getBaseValue() != gravityTarget) {
                grav.setBaseValue(gravityTarget);
                safeFall.setBaseValue(3.0D / (gravityTarget/GravityModifiers.EARTH_GRAV));
            }

            if (safeFall.getBaseValue() != safeFallTarget) {
                safeFall.setBaseValue(safeFallTarget);
            }
        }
    }

    private static void updatePlayerGravity(ServerPlayerEntity player) {
        if (player.isSpectator()) return;

        EntityAttributeInstance gravity =
                player.getAttributeInstance(EntityAttributes.GRAVITY);
        if (gravity == null) return;

        boolean onMoon = player.getEntityWorld().getRegistryKey().equals(ModDims.MOON_KEY);
        boolean inOrbit = player.getEntityWorld().getRegistryKey().equals(ModDims.ORBIT_KEY);

        double planet_grav = GravityModifiers.EARTH_GRAV;
        if (onMoon) planet_grav = GravityModifiers.MOON_GRAV;
        if (inOrbit) planet_grav = GravityModifiers.ORBIT_GRAV;

        if (gravity.getBaseValue() != planet_grav) {
            gravity.setBaseValue(planet_grav);
        }
    }

    private static void addGravityModifier(
            EntityAttributeInstance gravity,
            net.minecraft.util.Identifier id,
            double targetValue

    ) {
        double delta = targetValue - GravityModifiers.EARTH_GRAV;

        gravity.addPersistentModifier(
                new EntityAttributeModifier(id, delta, EntityAttributeModifier.Operation.ADD_VALUE)
        );
    }


}
