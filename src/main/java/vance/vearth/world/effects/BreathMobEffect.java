package vance.vearth.world.effects;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.InstantaneousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class BreathMobEffect extends InstantaneousMobEffect {
    public BreathMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(final @NonNull ServerLevel level, final LivingEntity mob, final int amplification) {
        int amount = mob.getMaxAirSupply();
        mob.setAirSupply(amount);
        return true;
    }

    @Override
    public void applyInstantaneousEffect(
            final @NonNull ServerLevel serverLevel,
            final @Nullable Entity source,
            final @Nullable Entity owner,
            final LivingEntity mob,
            final int amplification,
            final double scale
    ) {
            int amount = mob.getMaxAirSupply();
            mob.setAirSupply(amount);
    }
}
