package vance.vearth.mixin.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EyeblossomBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import vance.vearth.block.ModBlocks;

@Mixin(EyeblossomBlock.class)
public class EyeblossomMixin extends FlowerBlock {
    @Unique
    private final EyeblossomBlock.Type type;
    public EyeblossomMixin(Holder<MobEffect> suspiciousStewEffect, float effectSeconds, Properties properties, EyeblossomBlock.Type type) {
        super(suspiciousStewEffect, effectSeconds, properties);
        this.type = type;
    }

    @Override
    protected @NonNull InteractionResult useItemOn(@NonNull ItemStack itemStack, @NonNull BlockState state, @NonNull Level level, @NonNull BlockPos pos, Player player, @NonNull InteractionHand hand, @NonNull BlockHitResult hitResult) {
        ItemStack handStack = player.getItemInHand(hand);
        boolean isValid = !handStack.isEmpty() && handStack.getItem().equals(Items.ECHO_SHARD);
        if (isValid && !player.isCrouching() && type.emitSounds()){
            level.setBlock(pos, ModBlocks.OPEN_ECHOFLOWER.defaultBlockState(), 11);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
