package vance.vearth.mixin.gravity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vance.vearth.gravity.GravityModifiers;
import vance.vearth.world.dimension.ModDims;

@Mixin({
        Entity.class,
        ItemEntity.class,
        FallingBlockEntity.class,
        AbstractArrow.class,
        ThrowableProjectile.class,
        AbstractBoat.class,
        AbstractMinecart.class
})
public class EntityGravityMixin {

    @Inject(method = "getDefaultGravity", at =@At("RETURN"), cancellable = true)
    private void vearth$scaleEntityGravity(CallbackInfoReturnable<Double> cir) {
        Entity self = (Entity) (Object)this;

        if (self instanceof LivingEntity) return;

        if (self.isNoGravity()) return;

        double original = cir.getReturnValue();

        boolean onMoon = self.level().dimension().equals(ModDims.MOON_KEY);
        boolean inOrbit = self.level().dimension().equals(ModDims.ORBIT_KEY);

        if (inOrbit) {
            cir.setReturnValue(0.0D);
        } else if (onMoon) {
            cir.setReturnValue(original * (GravityModifiers.MOON_GRAV / GravityModifiers.EARTH_GRAV));
        }
    }

}
