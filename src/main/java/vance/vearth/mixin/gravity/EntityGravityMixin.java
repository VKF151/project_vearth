package vance.vearth.mixin.gravity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.entity.vehicle.AbstractBoatEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
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
        PersistentProjectileEntity.class,
        ThrownEntity.class,
        AbstractBoatEntity.class,
        AbstractMinecartEntity.class
})
public class EntityGravityMixin {

    @Inject(method = "getGravity", at =@At("RETURN"), cancellable = true)
    private void vearth$scaleEntityGravity(CallbackInfoReturnable<Double> cir) {
        Entity self = (Entity) (Object)this;

        if (self instanceof LivingEntity) return;

        if (self.hasNoGravity()) return;

        double original = cir.getReturnValue();

        boolean onMoon = self.getEntityWorld().getRegistryKey().equals(ModDims.MOON_KEY);
        boolean inOrbit = self.getEntityWorld().getRegistryKey().equals(ModDims.ORBIT_KEY);

        if (inOrbit) {
            cir.setReturnValue(0.0D);
        } else if (onMoon) {
            cir.setReturnValue(original * (GravityModifiers.MOON_GRAV / GravityModifiers.EARTH_GRAV));
        }
    }

}
