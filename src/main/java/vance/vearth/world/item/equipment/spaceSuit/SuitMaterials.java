package vance.vearth.world.item.equipment.spaceSuit;

import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.item.equipment.trim.MaterialAssetGroup;
import vance.vearth.Project_vearth;
import vance.vearth.resources.registry.ModRegistries;

public class SuitMaterials {
    public static final ResourceKey<SuitMaterial> QUARTZ_KEY = registryKey("quartz");
    public static final ResourceKey<SuitMaterial> IRON_KEY = registryKey("iron");
    public static final ResourceKey<SuitMaterial> NETHERITE_KEY = registryKey("netherite");
    public static final ResourceKey<SuitMaterial> REDSTONE_KEY = registryKey("redstone");
    public static final ResourceKey<SuitMaterial> COPPER_KEY = registryKey("copper");
    public static final ResourceKey<SuitMaterial> GOLD_KEY = registryKey("gold");
    public static final ResourceKey<SuitMaterial> EMERALD_KEY = registryKey("emerald");
    public static final ResourceKey<SuitMaterial> DIAMOND_KEY = registryKey("diamond");
    public static final ResourceKey<SuitMaterial> LAPIS_KEY = registryKey("lapis");
    public static final ResourceKey<SuitMaterial> AMETHYST_KEY = registryKey("amethyst");
    public static final ResourceKey<SuitMaterial> RESIN_KEY = registryKey("resin");

    public static final SuitMaterial QUARTZ = register(QUARTZ_KEY, Style.EMPTY.withColor(14931140), MaterialAssetGroup.QUARTZ);
    public static final SuitMaterial IRON = register(IRON_KEY, Style.EMPTY.withColor(15527148), MaterialAssetGroup.IRON);
    public static final SuitMaterial NETHERITE = register(NETHERITE_KEY, Style.EMPTY.withColor(6445145), MaterialAssetGroup.NETHERITE);
    public static final SuitMaterial REDSTONE = register(REDSTONE_KEY, Style.EMPTY.withColor(9901575), MaterialAssetGroup.REDSTONE);
    public static final SuitMaterial COPPER = register(COPPER_KEY, Style.EMPTY.withColor(11823181), MaterialAssetGroup.COPPER);
    public static final SuitMaterial GOLD = register(GOLD_KEY, Style.EMPTY.withColor(14594349), MaterialAssetGroup.GOLD);
    public static final SuitMaterial EMERALD = register(EMERALD_KEY, Style.EMPTY.withColor(1155126), MaterialAssetGroup.EMERALD);
    public static final SuitMaterial DIAMOND = register(DIAMOND_KEY, Style.EMPTY.withColor(7269586), MaterialAssetGroup.DIAMOND);
    public static final SuitMaterial LAPIS = register(LAPIS_KEY, Style.EMPTY.withColor(4288151), MaterialAssetGroup.LAPIS);
    public static final SuitMaterial AMETHYST = register(AMETHYST_KEY, Style.EMPTY.withColor(10116294), MaterialAssetGroup.AMETHYST);
    public static final SuitMaterial RESIN = register(RESIN_KEY, Style.EMPTY.withColor(16545810), MaterialAssetGroup.RESIN);


    private static SuitMaterial register(final ResourceKey<SuitMaterial> registryKey, final Style hoverTextStyle, final MaterialAssetGroup assets) {

        Component description = Component.translatable(Util.makeDescriptionId("trim_material", registryKey.identifier())).withStyle(hoverTextStyle);
        return Registry.register(ModRegistries.suitMaterials, registryKey, new SuitMaterial(assets, description));
    }

    private static ResourceKey<SuitMaterial> registryKey(final String id) {
        return ResourceKey.create(ModRegistries.SUIT_MATERIAL, Identifier.fromNamespaceAndPath(Project_vearth.MOD_ID, id));
    }

    public static void initialize() {}
}
