package vance.vearth.block.custom;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.TrailParticleOption;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.BlockUtil;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.entity.animal.bee.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import vance.vearth.block.ModBlocks;
import vance.vearth.world.dimension.ModDims;
import vance.vearth.world.dimension.portal.VearthPortalForcer;

import java.util.Objects;
import java.util.Optional;

import static vance.vearth.block.custom.VearthPortalBlock.AXIS;

public class EchoFlowerBlock extends FlowerBlock implements Portal {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final MapCodec<EchoFlowerBlock> CODEC = RecordCodecBuilder.mapCodec(
            i -> i.group(Codec.BOOL.fieldOf("open").forGetter(e -> e.type.open), propertiesCodec()).apply(i, EchoFlowerBlock::new)
    );
    private final EchoFlowerBlock.Type type;

    @Override
    public @NonNull MapCodec<? extends EchoFlowerBlock> codec() {
        return CODEC;
    }

    public EchoFlowerBlock(final EchoFlowerBlock.Type type, final BlockBehaviour.Properties properties) {
        super(type.effect, type.effectDuration, properties);
        this.type = type;
    }

    public EchoFlowerBlock(final boolean open, final BlockBehaviour.Properties properties) {
        super(EchoFlowerBlock.Type.fromBoolean(open).effect, EchoFlowerBlock.Type.fromBoolean(open).effectDuration, properties);
        this.type = EchoFlowerBlock.Type.fromBoolean(open);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, @NonNull BlockGetter level, @NonNull BlockPos pos) {
        return state.is(Blocks.SCULK)|| state.is(ModBlocks.REGOLITH) || state.is(BlockTags.SUPPORTS_VEGETATION) || state.is(Blocks.SMOOTH_BASALT);
    }

    @Override
    protected @NonNull InteractionResult useItemOn(@NonNull ItemStack itemStack, @NonNull BlockState state, @NonNull Level level, @NonNull BlockPos pos, Player player, @NonNull InteractionHand hand, @NonNull BlockHitResult hitResult) {
        ItemStack handStack = player.getItemInHand(hand);
        boolean isValid = !handStack.isEmpty() && handStack.getItem().equals(Items.ENDER_PEARL);
        if (isValid && !player.isCrouching() && player.canUsePortal(false) && this.type.open){
            player.setAsInsidePortal(this, pos);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void animateTick(final @NonNull BlockState state, final @NonNull Level level, final @NonNull BlockPos pos, final @NonNull RandomSource random) {
        if (this.type.emitSounds() && random.nextInt(700) == 0) {
            BlockState below = level.getBlockState(pos.below());
            if (below.is(Blocks.SCULK)) {
                level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.EYEBLOSSOM_IDLE, SoundSource.AMBIENT, 1.0F, 1.0F, false);
            }
        }
    }

    @Override
    protected void randomTick(final @NonNull BlockState state, final @NonNull ServerLevel level, final @NonNull BlockPos pos, final @NonNull RandomSource random) {
        if (this.tryChangingState(state, level, pos, random)) {
            level.playSound(null, pos, this.type.transform().longSwitchSound, SoundSource.BLOCKS, 1.0F, 1.0F);
        }

        super.randomTick(state, level, pos, random);
    }

    @Override
    protected void tick(final @NonNull BlockState state, final @NonNull ServerLevel level, final @NonNull BlockPos pos, final @NonNull RandomSource random) {
        if (this.tryChangingState(state, level, pos, random)) {
            level.playSound(null, pos, this.type.transform().shortSwitchSound, SoundSource.BLOCKS, 1.0F, 1.0F);
        }

        super.tick(state, level, pos, random);
    }

    private boolean tryChangingState(final BlockState state, final ServerLevel level, final BlockPos pos, final RandomSource random) {
        boolean shouldBeOpen = level.environmentAttributes().getValue(EnvironmentAttributes.EYEBLOSSOM_OPEN, pos).toBoolean(this.type.open);
        if (shouldBeOpen == this.type.open) {
            return false;
        }

        EchoFlowerBlock.Type newType = this.type.transform();
        level.setBlock(pos, newType.state(), 3);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));
        newType.spawnTransformParticle(level, pos, random);
        BlockPos.betweenClosed(pos.offset(-3, -2, -3), pos.offset(3, 2, 3)).forEach(nearby -> {
            BlockState nearbyState = level.getBlockState(nearby);
            if (nearbyState == state) {
                double distance = Math.sqrt(pos.distSqr(nearby));
                int delay = random.nextIntBetweenInclusive((int)(distance * 5.0), (int)(distance * 10.0));
                level.scheduleTick(nearby, state.getBlock(), delay);
            }
        });
        return true;
    }

    @Override
    protected void entityInside(
            final @NonNull BlockState state, final Level level, final @NonNull BlockPos pos, final @NonNull Entity entity, final @NonNull InsideBlockEffectApplier effectApplier, final boolean isPrecise
    ) {
        if (!level.isClientSide()
                && level.getDifficulty() != Difficulty.PEACEFUL
                && entity instanceof Bee bee
                && Bee.attractsBees(state)
                && !bee.hasEffect(MobEffects.POISON)) {
            bee.addEffect(Objects.requireNonNull(this.getBeeInteractionEffect()));
        }
    }

    @Override
    public MobEffectInstance getBeeInteractionEffect() {
        return new MobEffectInstance(MobEffects.POISON, 25);
    }

    @Override
    public @Nullable TeleportTransition getPortalDestination(final ServerLevel currentLevel, final @NonNull Entity entity, final @NonNull BlockPos portalEntryPos) {
        ResourceKey<Level> newDimension = currentLevel.dimension() == ModDims.MOON_KEY ? Level.OVERWORLD : ModDims.MOON_KEY;
        ServerLevel newLevel = currentLevel.getServer().getLevel(newDimension);
        if (newLevel == null) {
            return null;
        }

        boolean toMoon = newLevel.dimension() == ModDims.MOON_KEY;
        WorldBorder newWorldBorder = newLevel.getWorldBorder();
        double teleportationScale = DimensionType.getTeleportationScale(currentLevel.dimensionType(), newLevel.dimensionType());
        BlockPos approximateExitPos = newWorldBorder.clampToBounds(entity.getX() * teleportationScale, entity.getY(), entity.getZ() * teleportationScale);
        return this.getExitPortal(newLevel, entity, portalEntryPos, approximateExitPos, toMoon, newWorldBorder);
    }

    private @Nullable TeleportTransition getExitPortal(
            final ServerLevel newLevel,
            final Entity entity,
            final BlockPos portalEntryPos,
            final BlockPos approximateExitPos,
            final boolean toMoon,
            final WorldBorder worldBorder
    ) {
        VearthPortalForcer vearthPortalForcer = new VearthPortalForcer(newLevel);
        Optional<BlockPos> exitPortalPos = vearthPortalForcer.findClosestPortalPosition(approximateExitPos, toMoon, worldBorder);
        BlockUtil.FoundRectangle exitPortal;
        TeleportTransition.PostTeleportTransition post;
        if (exitPortalPos.isPresent()) {
            BlockPos pos = exitPortalPos.get();
            BlockState portalState = newLevel.getBlockState(pos);
            exitPortal = BlockUtil.getLargestRectangleAround(
                    pos, Direction.Axis.X, 21, Direction.Axis.Y, 21, blockPos -> newLevel.getBlockState(blockPos) == portalState
            );
            post = TeleportTransition.PLAY_PORTAL_SOUND.then(e -> e.placePortalTicket(pos));
        } else {
            Direction.Axis sourcePortalAxis = entity.level().getBlockState(portalEntryPos).getOptionalValue(AXIS).orElse(Direction.Axis.X);
            Optional<BlockUtil.FoundRectangle> createdExit = vearthPortalForcer.createPortal(approximateExitPos, sourcePortalAxis);
            if (createdExit.isEmpty()) {
                LOGGER.error("Unable to create a portal, likely target out of worldborder");
                return null;
            }

            exitPortal = createdExit.get();
            post = TeleportTransition.PLAY_PORTAL_SOUND.then(TeleportTransition.PLACE_PORTAL_TICKET);
        }

        return getDimensionTransitionFromExit(entity, portalEntryPos, exitPortal, newLevel, post);
    }
    private static TeleportTransition getDimensionTransitionFromExit(
            final Entity entity,
            final BlockPos portalEntryPos,
            final BlockUtil.FoundRectangle exitPortal,
            final ServerLevel newLevel,
            final TeleportTransition.PostTeleportTransition postTeleportTransition
    ) {
        BlockState blockState = entity.level().getBlockState(portalEntryPos);
        Direction.Axis axis;
        Vec3 offset;
        if (blockState.hasProperty(BlockStateProperties.HORIZONTAL_AXIS)) {
            axis = blockState.getValue(BlockStateProperties.HORIZONTAL_AXIS);
            BlockUtil.FoundRectangle portalArea = BlockUtil.getLargestRectangleAround(
                    portalEntryPos, axis, 21, Direction.Axis.Y, 21, pos -> entity.level().getBlockState(pos) == blockState
            );
            offset = entity.getRelativePortalPosition(axis, portalArea);
        } else {
            axis = Direction.Axis.X;
            offset = new Vec3(0.5, 0.0, 0.0);
        }

        return createDimensionTransition(newLevel, exitPortal, axis, offset, entity, postTeleportTransition);
    }

    private static TeleportTransition createDimensionTransition(
            final ServerLevel newLevel,
            final BlockUtil.FoundRectangle foundRectangle,
            final Direction.Axis portalAxis,
            final Vec3 offset,
            final Entity entity,
            final TeleportTransition.PostTeleportTransition postTeleportTransition
    ) {
        BlockPos bottomLeft = foundRectangle.minCorner;
        BlockState blockState = newLevel.getBlockState(bottomLeft);
        Direction.Axis axis = blockState.getOptionalValue(BlockStateProperties.HORIZONTAL_AXIS).orElse(Direction.Axis.X);
        double width = foundRectangle.axis1Size;
        double height = foundRectangle.axis2Size;
        EntityDimensions dimensions = entity.getDimensions(entity.getPose());
        int outputRotation = portalAxis == axis ? 0 : 90;
        double offsetRight = dimensions.width() / 2.0 + (width - dimensions.width()) * offset.x();
        double offsetUp = (height - dimensions.height()) * offset.y();
        double offsetForward = 0.5 + offset.z();
        boolean xAligned = axis == Direction.Axis.X;
        Vec3 targetPos = new Vec3(
                bottomLeft.getX() + (xAligned ? offsetRight : offsetForward), bottomLeft.getY() + offsetUp, bottomLeft.getZ() + (xAligned ? offsetForward : offsetRight)
        );
        Vec3 collisionFreePos = PortalShape.findCollisionFreePosition(targetPos, newLevel, entity, dimensions);
        return new TeleportTransition(
                newLevel, collisionFreePos, Vec3.ZERO, outputRotation, 0.0F, Relative.union(Relative.DELTA, Relative.ROTATION), postTeleportTransition
        );
    }

    public enum Type {
        OPEN(true, MobEffects.DARKNESS, 11.0F, SoundEvents.EYEBLOSSOM_OPEN_LONG, SoundEvents.EYEBLOSSOM_OPEN, 16545810),
        CLOSED(false, MobEffects.WITHER, 7.0F, SoundEvents.EYEBLOSSOM_CLOSE_LONG, SoundEvents.EYEBLOSSOM_CLOSE, 6250335);

        private final boolean open;
        private final Holder<MobEffect> effect;
        private final float effectDuration;
        private final SoundEvent longSwitchSound;
        private final SoundEvent shortSwitchSound;
        private final int particleColor;

        Type(
                final boolean open,
                final Holder<MobEffect> effect,
                final float duration,
                final SoundEvent longSwitchSound,
                final SoundEvent shortSwitchSound,
                final int particleColor
        ) {
            this.open = open;
            this.effect = effect;
            this.effectDuration = duration;
            this.longSwitchSound = longSwitchSound;
            this.shortSwitchSound = shortSwitchSound;
            this.particleColor = particleColor;
        }

        public Block block() {
            return this.open ? ModBlocks.OPEN_ECHOFLOWER : ModBlocks.CLOSED_ECHOFLOWER;
        }

        public BlockState state() {
            return this.block().defaultBlockState();
        }

        public EchoFlowerBlock.Type transform() {
            return fromBoolean(!this.open);
        }

        public boolean emitSounds() {
            return this.open;
        }

        public static EchoFlowerBlock.Type fromBoolean(final boolean open) {
            return open ? OPEN : CLOSED;
        }

        public void spawnTransformParticle(final ServerLevel level, final BlockPos pos, final RandomSource random) {
            Vec3 start = Vec3.atCenterOf(pos);
            double lifetime = 0.5 + random.nextDouble();
            Vec3 velocity = new Vec3(random.nextDouble() - 0.5, random.nextDouble() + 1.0, random.nextDouble() - 0.5);
            Vec3 target = start.add(velocity.scale(lifetime));
            TrailParticleOption particle = new TrailParticleOption(target, this.particleColor, (int)(20.0 * lifetime));
            level.sendParticles(particle, start.x, start.y, start.z, 1, 0.0, 0.0, 0.0, 0.0);
        }
    }
}
