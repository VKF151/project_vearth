package vance.vearth.mixin.entity;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vance.vearth.components.ModComponents;
import vance.vearth.resources.Identifier.ModPotionIds;
import vance.vearth.world.item.equipment.spaceSuit.RespirantStorage;

import java.util.Objects;

@Mixin(ArmorStand.class)
public class ArmorStandMixin {

    @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
    public void fillSuit(Player player, InteractionHand hand, Vec3 location, CallbackInfoReturnable<InteractionResult> cir) {
        ArmorStand instance = (ArmorStand) (Object) this;
        ItemStack itemStack = player.getItemInHand(hand);

        if (player.isSpectator()) {
            cir.setReturnValue(InteractionResult.SUCCESS);
        }

        if (player.level().isClientSide()) {
            cir.setReturnValue(InteractionResult.SUCCESS_SERVER);
        }
        ItemStack suit = instance.getItemBySlot(EquipmentSlot.CHEST);
        Holder<Potion> potion = null;
        if (itemStack.has(DataComponents.POTION_CONTENTS)) {
            potion = Objects.requireNonNull(itemStack.get(DataComponents.POTION_CONTENTS)).potion().get();

        }
        if ( suit != null && itemStack.has(ModComponents.RESPIRANT_STORAGE) && suit.has(ModComponents.RESPIRANT_STORAGE) && instance.level() instanceof ServerLevel) {
            int storedOxygen = suit.get(ModComponents.RESPIRANT_STORAGE).respirantAmount();
            int maxOxygen = suit.get(ModComponents.RESPIRANT_STORAGE).maxRespirantAmount();
            int supplyOxygen = itemStack.get(ModComponents.RESPIRANT_STORAGE).respirantAmount();

            if (storedOxygen < maxOxygen && supplyOxygen > 0) {
                RespirantStorage respirantStorage = new RespirantStorage(Math.min(maxOxygen, storedOxygen + supplyOxygen), maxOxygen);
                RespirantStorage drainedRespirant = new RespirantStorage(Math.max(0, supplyOxygen - (maxOxygen - storedOxygen)), maxOxygen);
                suit.set(ModComponents.RESPIRANT_STORAGE, respirantStorage);
                if ((storedOxygen + supplyOxygen) <= maxOxygen && itemStack.is(Items.POTION)) {
                    itemStack.remove(ModComponents.RESPIRANT_STORAGE);
                    if (potion != null && potion.is(ModPotionIds.BREATH)) {
                        itemStack.remove(DataComponents.POTION_CONTENTS);
                    }
                } else {
                    itemStack.set(ModComponents.RESPIRANT_STORAGE, drainedRespirant);
                }
                cir.setReturnValue(InteractionResult.SUCCESS);
            }

        }

    }

}
