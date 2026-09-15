package vance.vearth.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import org.jspecify.annotations.Nullable;
import vance.vearth.block.ModBlocks;
import vance.vearth.block.custom.EndoseleneHyphaeBlock;

import java.util.Collection;

public interface EndoseleneBehaviour {
    EndoseleneBehaviour DEFAULT = new EndoseleneBehaviour() {
        @Override
        public boolean attemptSpreadVein(
                final LevelAccessor level, final BlockPos pos, final BlockState state, final @Nullable Collection<Direction> facings, final boolean postProcess
        ) {
            if (facings == null) {
                return ((EndoseleneHyphaeBlock) ModBlocks.ENDOSELENE_HYPHAE).getSameSpaceSpreader().spreadAll(level.getBlockState(pos), level, pos, postProcess) > 0L;
            } else if (!facings.isEmpty()) {
                return !state.isAir() && !state.getFluidState().is(Fluids.WATER) ? false : EndoseleneHyphaeBlock.regrow(level, pos, state, facings);
            } else {
                return EndoseleneBehaviour.super.attemptSpreadVein(level, pos, state, facings, postProcess);
            }
        }

        @Override
        public int attemptUseCharge(
                final EndoseleneSpreader.ChargeCursor cursor,
                final LevelAccessor level,
                final BlockPos originPos,
                final RandomSource random,
                final EndoseleneSpreader spreader,
                final boolean spreadVeins
        ) {
            return cursor.getDecayDelay() > 0 ? cursor.getCharge() : 0;
        }

        @Override
        public int updateDecayDelay(final int age) {
            return Math.max(age - 1, 0);
        }
    };

    default byte getEndoseleneSpreadDelay() {
        return 1;
    }

    default void onDischarged(final LevelAccessor level, final BlockState state, final BlockPos pos, final RandomSource random) {
    }

    default boolean depositCharge(final LevelAccessor level, final BlockPos pos, final RandomSource random) {
        return false;
    }

    default boolean attemptSpreadVein(
            final LevelAccessor level, final BlockPos pos, final BlockState state, final @Nullable Collection<Direction> facings, final boolean postProcess
    ) {
        return ((MultifaceSpreadeableBlock) ModBlocks.ENDOSELENE_HYPHAE).getSpreader().spreadAll(state, level, pos, postProcess) > 0L;
    }

    default boolean canChangeBlockStateOnSpread() {
        return true;
    }

    default int updateDecayDelay(final int age) {
        return 1;
    }

    int attemptUseCharge(
            EndoseleneSpreader.ChargeCursor cursor, LevelAccessor level, BlockPos originPos, RandomSource random, EndoseleneSpreader spreader, boolean spreadVeins
    );
}
