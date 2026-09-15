package vance.vearth.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import org.jspecify.annotations.NonNull;
import vance.vearth.world.level.block.EndoseleneBehaviour;
import vance.vearth.world.level.block.EndoseleneSpreader;

import java.util.Objects;
import java.util.stream.Stream;

public record EndoselenePatchFeature(int chargeCount, int amountPerCharge, int spreadAttempts, int growthRounds, int spreadRounds) implements Feature {
    public static final MapCodec<EndoselenePatchFeature> CODEC = RecordCodecBuilder.mapCodec((i) -> i.group(Codec.intRange(1, 32).fieldOf("charge_count").forGetter(EndoselenePatchFeature::chargeCount), Codec.intRange(1, 500).fieldOf("amount_per_charge").forGetter(EndoselenePatchFeature::amountPerCharge), Codec.intRange(1, 64).fieldOf("spread_attempts").forGetter(EndoselenePatchFeature::spreadAttempts), Codec.intRange(0, 8).fieldOf("growth_rounds").forGetter(EndoselenePatchFeature::growthRounds), Codec.intRange(0, 8).fieldOf("spread_rounds").forGetter(EndoselenePatchFeature::spreadRounds)).apply(i, EndoselenePatchFeature::new));

    public @NonNull MapCodec<EndoselenePatchFeature> codec() {
        return CODEC;
    }

    public boolean place(final @NonNull WorldGenLevel level, final @NonNull ChunkGenerator chunkGenerator, final @NonNull RandomSource random, final @NonNull BlockPos origin) {
        if (!this.canSpreadFrom(level, origin)) {
            return false;
        } else {
            EndoseleneSpreader spreader = EndoseleneSpreader.createWorldGenSpreader();
            int totalRounds = this.spreadRounds + this.growthRounds;

            for(int round = 0; round < totalRounds; ++round) {
                for(int i = 0; i < this.chargeCount; ++i) {
                    spreader.addCursors(origin, this.amountPerCharge);
                }

                boolean spreadVeins = round < this.spreadRounds;

                for(int i = 0; i < this.spreadAttempts; ++i) {
                    spreader.updateCursors(level, origin, random, spreadVeins);
                }

                spreader.clear();
            }

            return true;
        }
    }

    private boolean canSpreadFrom(final LevelAccessor level, final BlockPos origin) {
        BlockState start = level.getBlockState(origin);
        if (start.getBlock() instanceof EndoseleneBehaviour) {
            return true;
        } else if (!start.isAir() && (!start.is(Blocks.WATER) || !start.getFluidState().isSource())) {
            return false;
        } else {
            Stream var10000 = Direction.stream();
            Objects.requireNonNull(origin);
            return var10000.map((direction) -> origin.relative((Direction) direction)).anyMatch((pos) -> level.getBlockState((BlockPos) pos).isCollisionShapeFullBlock(level, (BlockPos) pos));
        }
    }
}
