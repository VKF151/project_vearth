package vance.vearth.world.item.equipment.spaceSuit;

import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import vance.vearth.Project_vearth;
import vance.vearth.resources.registry.ModRegistries;

public class SuitDesigns {
    public static final ResourceKey<SuitDesign> FULL_KEY = registryKey("full");

    public static final SuitDesign FULL = register(FULL_KEY);


    public static SuitDesign register(ResourceKey<SuitDesign> registryKey) {
        SuitDesign design = new SuitDesign(
                defaultAssetId(registryKey), Component.translatable(Util.makeDescriptionId("suit_design", registryKey.identifier())), false
        );
        return Registry.register(ModRegistries.suitDesigns, registryKey.identifier(), design);
    }

    private static ResourceKey<SuitDesign> registryKey(final String id) {
        return ResourceKey.create(ModRegistries.SUIT_DESIGN, Identifier.fromNamespaceAndPath(Project_vearth.MOD_ID, id));
    }

    public static Identifier defaultAssetId(final ResourceKey<SuitDesign> registryKey) {
        return registryKey.identifier();
    }

    public static void initialize() {}
}
