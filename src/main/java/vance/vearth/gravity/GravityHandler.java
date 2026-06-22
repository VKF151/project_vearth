package vance.vearth.gravity;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import vance.vearth.world.dimension.ModDims;

public class GravityHandler {

    private GravityHandler() {}

    public static void register() {
        ServerTickEvents.END_LEVEL_TICK.register(GravityHandler::onWorldTick);
    }

    private static void onServerTick(MinecraftServer server) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            updatePlayerGravity(player);
        }
    }
    private static void onWorldTick(ServerLevel world) {
        final boolean onMoon  = world.dimension().equals(ModDims.MOON_KEY);
        final boolean inOrbit = world.dimension().equals(ModDims.ORBIT_KEY);

        final double gravityTarget = inOrbit ? GravityModifiers.ORBIT_GRAV : (onMoon ? GravityModifiers.MOON_GRAV : GravityModifiers.EARTH_GRAV);

        final  double ratio = (gravityTarget / GravityModifiers.EARTH_GRAV);
        final double safeFallTarget = (ratio <= 0.0D) ? 1_000_000.0D : (3.0D / ratio);

        for (Entity e : world.getAllEntities()) {
            if (!(e instanceof LivingEntity living)) continue;

            AttributeInstance grav = living.getAttribute(Attributes.GRAVITY);
            AttributeInstance safeFall = living.getAttribute(Attributes.SAFE_FALL_DISTANCE);
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

    private static void updatePlayerGravity(ServerPlayer player) {
        if (player.isSpectator()) return;

        AttributeInstance gravity =
                player.getAttribute(Attributes.GRAVITY);
        if (gravity == null) return;

        boolean onMoon = player.level().dimension().equals(ModDims.MOON_KEY);
        boolean inOrbit = player.level().dimension().equals(ModDims.ORBIT_KEY);

        double planet_grav = GravityModifiers.EARTH_GRAV;
        if (onMoon) planet_grav = GravityModifiers.MOON_GRAV;
        if (inOrbit) planet_grav = GravityModifiers.ORBIT_GRAV;

        if (gravity.getBaseValue() != planet_grav) {
            gravity.setBaseValue(planet_grav);
        }
    }

    private static void addGravityModifier(
            AttributeInstance gravity,
            net.minecraft.resources.Identifier id,
            double targetValue

    ) {
        double delta = targetValue - GravityModifiers.EARTH_GRAV;

        gravity.addPermanentModifier(
                new AttributeModifier(id, delta, AttributeModifier.Operation.ADD_VALUE)
        );
    }


}
