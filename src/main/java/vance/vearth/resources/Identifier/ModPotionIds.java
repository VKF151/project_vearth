package vance.vearth.resources.Identifier;

import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import vance.vearth.Project_vearth;

public class ModPotionIds {
    public static final ResourceKey<Potion> BREATH = create("breath");

    private static ResourceKey<Potion> create(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(Project_vearth.MOD_ID, name);
        return ResourceKey.create(Registries.POTION, id);
    }
}
