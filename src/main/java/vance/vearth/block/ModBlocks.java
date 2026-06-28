package vance.vearth.block;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import vance.vearth.block.custom.EchoFlowerBlock;
import vance.vearth.block.custom.VearthPortalBlock;
import vance.vearth.resources.Identifier.ModBlockItemId;
import vance.vearth.resources.Identifier.ModBlockItemIds;

import java.util.function.Function;

import static net.minecraft.world.level.block.Blocks.*;

public class ModBlocks {
    private static Block register(final ModBlockItemId id, final Function<BlockBehaviour.Properties, Block> factory, final BlockBehaviour.Properties properties) {
        return register(id.block(), factory, properties);
    }

    public static Block register(final ResourceKey<Block> id, final Function<BlockBehaviour.Properties, Block> factory, final BlockBehaviour.Properties properties) {
        Block block = factory.apply(properties.setId(id));
        return Registry.register(BuiltInRegistries.BLOCK, id, block);
    }

    public static final Block REGOLITH = register(ModBlockItemIds.REGOLITH, (p) ->
            new SandBlock(new ColorRGBA(10000060), p), BlockBehaviour.Properties.of()
            .mapColor(MapColor.SAND)
            .instrument(NoteBlockInstrument.SNARE)
            .strength(0.5F)
            .sound(SoundType.SAND));

    public static final Block VEARTH_PORTAL = register(
            ModBlockItemIds.VEARTH_PORTAL,
            VearthPortalBlock::new,
            BlockBehaviour.Properties.of().noCollision().randomTicks().strength(-1.0F).sound(SoundType.GLASS).lightLevel(statex -> 11).pushReaction(PushReaction.BLOCK)
    );

    public static final Block OPEN_ECHOFLOWER = register(
            ModBlockItemIds.OPEN_ECHOFLOWER,
            p -> new EchoFlowerBlock(EchoFlowerBlock.Type.OPEN, p),
            BlockBehaviour.Properties.of()
                    .mapColor(CREAKING_HEART.defaultMapColor())
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .pushReaction(PushReaction.DESTROY)
                    .randomTicks()
    );
    public static final Block CLOSED_ECHOFLOWER = register(
            ModBlockItemIds.CLOSED_ECHOFLOWER,
            p -> new EchoFlowerBlock(EchoFlowerBlock.Type.CLOSED, p),
            BlockBehaviour.Properties.of()
                    .mapColor(PALE_OAK_LEAVES.defaultMapColor())
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .pushReaction(PushReaction.DESTROY)
                    .randomTicks()
    );
    public static final Block POTTED_OPEN_ECHOFLOWER = register(
            ModBlockItemIds.POTTED_OPEN_ECHOFLOWER, p -> new FlowerPotBlock(OPEN_EYEBLOSSOM, p), flowerPotProperties().randomTicks()
    );
    public static final Block POTTED_CLOSED_ECHOFLOWER = register(
            ModBlockItemIds.POTTED_CLOSED_ECHOFLOWER, p -> new FlowerPotBlock(CLOSED_EYEBLOSSOM, p), flowerPotProperties().randomTicks()
    );

    public static void initialize() {}

}
