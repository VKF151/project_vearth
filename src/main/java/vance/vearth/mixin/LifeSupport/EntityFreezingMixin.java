package vance.vearth.mixin.LifeSupport;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vance.vearth.components.ModComponents;
import vance.vearth.world.dimension.ModDims;

@Mixin(LivingEntity.class)
public abstract class EntityFreezingMixin extends Entity {
    @Unique
    protected final RandomSource random = RandomSource.create();

    public EntityFreezingMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Inject(method = "baseTick", at = @At(value = "TAIL", target = "Lnet/minecraft/world/entity/Entity;baseTick()V"))
    private void freezingCheck(CallbackInfo ci) {
        LivingEntity instance = (LivingEntity) (Object) this;
        if (instance.isAlive() && !isCreativePlayer() && !instance.isSpectator() && instance.level() instanceof ServerLevel level) {
            if (instance.canFreeze() && instance.level().dimension().equals(ModDims.MOON_KEY) && !isWearingSealedSuit(instance)) {
                instance.setIsInPowderSnow(true);
                instance.setTicksFrozen(Math.min(instance.getTicksRequiredToFreeze(), instance.getTicksFrozen() + 2));
            }
            if (instance.level().dimension().equals(ModDims.MOON_KEY) && !suitHasOxygen(instance)) {
                instance.setAirSupply(decreaseAirSupplyFast(instance.getAirSupply()));
                if (shouldTakeSuffocationDamage()) {
                    instance.setAirSupply(0);
                    level.broadcastEntityEvent(instance, (byte) 67);
                    instance.hurtServer(level, instance.damageSources().inWall(), 2.0F);
                }
            }
        }
    }

    @Inject(method = "increaseAirSupply", at = @At(value = "HEAD", target = "Lnet/minecraft/world/entity/Entity;increaseAirSupply(I)"), cancellable = true)
    void oxygenCheck(int currentSupply, CallbackInfoReturnable<Integer> cir){
        LivingEntity instance = (LivingEntity) (Object) this;
        if (instance.level().dimension().equals(ModDims.MOON_KEY) && !suitHasOxygen(instance) && !isCreativePlayer() && !instance.isSpectator()) {
            cir.setReturnValue(currentSupply);
        }
    }

    @Inject(method = "decreaseAirSupply", at = @At(value = "HEAD", target = "Lnet/minecraft/world/entity/Entity;decreaseAirSupply(I)"), cancellable = true)
    void oxygenCheckInWater(int currentSupply, CallbackInfoReturnable<Integer> cir){
        LivingEntity instance = (LivingEntity) (Object) this;
        if (instance.isAlive() && !isCreativePlayer() && !instance.isSpectator() && instance.level() instanceof ServerLevel level) {
            if (this.isEyeInFluid(FluidTags.WATER)
                    && !level.getBlockState(BlockPos.containing(this.getX(), this.getEyeY(), this.getZ())).is(Blocks.BUBBLE_COLUMN)
                    && suitHasOxygen(instance)) {
                cir.setReturnValue(currentSupply);
            }
        }
    }

    @Unique
    private static boolean isWearingSealedSuit(LivingEntity player) {
        return player.getItemBySlot(EquipmentSlot.HEAD).getComponents().has(ModComponents.AIR_TIGHT)
                && player.getItemBySlot(EquipmentSlot.CHEST).getComponents().has(ModComponents.AIR_TIGHT)
                && player.getItemBySlot(EquipmentSlot.LEGS).getComponents().has(ModComponents.AIR_TIGHT)
                && player.getItemBySlot(EquipmentSlot.FEET).getComponents().has(ModComponents.AIR_TIGHT);
    }

    @Unique
    private static boolean suitHasOxygen(LivingEntity player) {
        return isWearingSealedSuit(player) && player.getItemBySlot(EquipmentSlot.CHEST).get(ModComponents.OXYGEN_STORAGE) != null && player.getItemBySlot(EquipmentSlot.CHEST).get(ModComponents.OXYGEN_STORAGE) > 0;
    }

    @Unique
    private int suffocationDamageThreshold;

    @Unique
    protected int decreaseAirSupplyFast(final int currentSupply) {
        LivingEntity instance = (LivingEntity) (Object) this;
        int airLossPerTick;
        AttributeInstance respiration = instance.getAttribute(Attributes.OXYGEN_BONUS);
        double oxygenBonus;
        if (respiration != null) {
            oxygenBonus = respiration.getValue();
        } else {
            oxygenBonus = 0.0;
        }
        if (instance.getBlockStateOn().is(Blocks.SCULK)) {
            airLossPerTick = 2;
        }else {
            airLossPerTick = 20;
        }
        suffocationDamageThreshold = airLossPerTick * (-20);
        return oxygenBonus > 0.0 && random.nextDouble() >= 1.0 / (oxygenBonus + 1.0) ? currentSupply : currentSupply - airLossPerTick;
    }


    @Unique
    protected boolean shouldTakeSuffocationDamage() {
        LivingEntity instance = (LivingEntity) (Object) this;
        return instance.getAirSupply() <= suffocationDamageThreshold;
    }

    @Unique
    public boolean isCreativePlayer() {
        LivingEntity instance = (LivingEntity) (Object) this;
        return instance instanceof Player player && player.getAbilities().instabuild;
    }



}
