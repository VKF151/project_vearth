package vance.vearth.item.ids;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import vance.vearth.Project_vearth;

public record ModBlockItemId(ResourceKey<Block> block, ResourceKey<Item> item) {
    public static ModBlockItemId create(final Identifier blockId, final Identifier itemId) {
        return new ModBlockItemId(ResourceKey.create(Registries.BLOCK, blockId), ResourceKey.create(Registries.ITEM, itemId));
    }

    public static ModBlockItemId create(final String name) {
        Identifier id = Identifier.fromNamespaceAndPath(Project_vearth.MOD_ID ,name);
        return create(id, id);
    }
}
