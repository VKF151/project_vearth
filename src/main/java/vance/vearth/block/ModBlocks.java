package vance.vearth.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SandBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.ColorCode;
import net.minecraft.util.Identifier;
import vance.vearth.Project_vearth;

public class ModBlocks {
    public static Block register(Block block, String name, boolean shouldRegisterItem) {
        Identifier id = Identifier.of(Project_vearth.MOD_ID, name);

        if (shouldRegisterItem) {
            BlockItem blockItem = new BlockItem(block, new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Project_vearth.MOD_ID, name))));
            Registry.register(Registries.ITEM, id, blockItem);
        }

        return Registry.register(Registries.BLOCK, id, block);
    }

    public static final Block MOON_SOIL = register(
            new SandBlock(new ColorCode(10000060) ,AbstractBlock.Settings
                    .create()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Project_vearth.MOD_ID, "moon_soil")))
                    .sounds(BlockSoundGroup.ROOTED_DIRT)
                    .strength(0.5F, 0.5F)),
                    "moon_soil",
                    true
    );

    public static void initialize() {}

}
