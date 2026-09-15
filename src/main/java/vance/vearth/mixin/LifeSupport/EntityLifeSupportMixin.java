package vance.vearth.mixin.LifeSupport;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import vance.vearth.world.item.equipment.spaceSuit.RespirantStorage;

@Mixin(LivingEntity.class)
public abstract class EntityLifeSupportMixin extends Entity {

    @Unique
    protected final RandomSource random = RandomSource.create();

    public EntityLifeSupportMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Inject(method = "baseTick", at = @At(value = "TAIL", target = "Lnet/minecraft/world/entity/Entity;baseTick()V"))
    private void lifeSupportCheck(CallbackInfo ci) {
        LivingEntity instance = (LivingEntity) (Object) this;
        if (instance.isAlive() && !instance.is(EntityTypes.ARMOR_STAND) && !isCreativePlayer() && !instance.isSpectator() && instance.level() instanceof ServerLevel level) {
            ItemStack chestStack = instance.getItemBySlot(EquipmentSlot.CHEST);
            if (instance.canFreeze() && instance.level().dimension().equals(ModDims.VEARTH_KEY) && !isWearingSpaceSuit(instance)) {
                instance.setIsInPowderSnow(true);
                instance.setTicksFrozen(Math.min(instance.getTicksRequiredToFreeze(), instance.getTicksFrozen() + 2));
            }
            if (instance.level().dimension().equals(ModDims.VEARTH_KEY) && !suitHasOxygen(instance)) {
                instance.setAirSupply(decreaseAirSupplyFast(instance.getAirSupply()));
                if (shouldTakeSuffocationDamage()) {
                    instance.setAirSupply(0);
                    level.broadcastEntityEvent(instance, (byte) 67);
                    instance.hurtServer(level, instance.damageSources().inWall(), 2.0F);
                }
            } else if (instance.level().dimension().equals(ModDims.VEARTH_KEY) && suitHasOxygen(instance) && chestStack.get(ModComponents.RESPIRANT_STORAGE) != null) {
                decreaseSuitOxygen(instance, getSuitOxygen(instance));
            }
        }
    }

    @Inject(method = "increaseAirSupply", at = @At(value = "HEAD", target = "Lnet/minecraft/world/entity/Entity;increaseAirSupply(I)"), cancellable = true)
    void oxygenCheck(int currentSupply, CallbackInfoReturnable<Integer> cir){
        LivingEntity instance = (LivingEntity) (Object) this;
        if (instance.level().dimension().equals(ModDims.VEARTH_KEY) && !suitHasOxygen(instance) && !isCreativePlayer() && !instance.isSpectator()) {
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
                decreaseSuitOxygen(instance, getSuitOxygen(instance));
                cir.setReturnValue(currentSupply);
            }
        }
    }

    @Unique
    private static boolean isWearingSpaceSuit(LivingEntity player) {
        return (player.getItemBySlot(EquipmentSlot.HEAD).getComponents().has(ModComponents.SUIT)
                && player.getItemBySlot(EquipmentSlot.CHEST).getComponents().has(ModComponents.SUIT)
                && player.getItemBySlot(EquipmentSlot.LEGS).getComponents().has(ModComponents.SUIT)
                && player.getItemBySlot(EquipmentSlot.FEET).getComponents().has(ModComponents.SUIT));
    }

    @Unique
    private static boolean suitHasOxygen(LivingEntity player) {
        return (isWearingSpaceSuit(player))
                && player.getItemBySlot(EquipmentSlot.CHEST).has(ModComponents.RESPIRANT_STORAGE)
                && getSuitOxygen(player) > 0;
    }
    @Unique
    private static int getSuitOxygen(LivingEntity player) {
        return player.getItemBySlot(EquipmentSlot.CHEST).get(ModComponents.RESPIRANT_STORAGE).respirantAmount();
    }

    @Unique
    private static void decreaseSuitOxygen(LivingEntity player, int currentSupply) {
        int airLossPerTick = 1;
        if (player.getItemBySlot(EquipmentSlot.CHEST).has(ModComponents.RESPIRANT_STORAGE)) {
            RespirantStorage newSupply = new RespirantStorage(currentSupply - airLossPerTick, player.getItemBySlot(EquipmentSlot.CHEST).get(ModComponents.RESPIRANT_STORAGE).maxRespirantAmount());
            player.getItemBySlot(EquipmentSlot.CHEST).set(ModComponents.RESPIRANT_STORAGE, newSupply);
        }
    }

    @Unique
    private int suffocationDamageThreshold;

    @Unique
    protected int decreaseAirSupplyFast(final int currentSupply) {
        LivingEntity instance = (LivingEntity) (Object) this;
        int airLossPerTick = 20;
        AttributeInstance respiration = instance.getAttribute(Attributes.OXYGEN_BONUS);
        double oxygenBonus;
        if (respiration != null) {
            oxygenBonus = respiration.getValue();
        } else {
            oxygenBonus = 0.0;
        }
        if (instance.getBlockStateOn().is(Blocks.SCULK)) {
            airLossPerTick = airLossPerTick/10;
        }else if (isWearingSpaceSuit(instance)){
            airLossPerTick = airLossPerTick/10;
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
