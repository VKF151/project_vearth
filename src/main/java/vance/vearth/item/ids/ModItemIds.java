package vance.vearth.item.ids;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import vance.vearth.Project_vearth;

public class ModItemIds {
    public static final ResourceKey<Item> MOD_ICON = create("mod_icon");
    public static final ResourceKey<Item> SPACE_SUIT_HELMET = create("space_suit_helmet");
    public static final ResourceKey<Item> SPACE_SUIT_CHESTPLATE = create("space_suit_chestplate");
    public static final ResourceKey<Item> SPACE_SUIT_LEGGINGS = create("space_suit_leggings");
    public static final ResourceKey<Item> SPACE_SUIT_BOOTS = create("space_suit_boots");

    private static ResourceKey<Item> create(final String name) {
        return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Project_vearth.MOD_ID, name));
    }
}
