package vance.vearth.world.item.equipment.spaceSuit;

import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
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

    public static final SuitMaterial QUARTZ = register(QUARTZ_KEY, Style.EMPTY.withColor(14931140), SuitMaterials.Palette.QUARTZ);
    public static final SuitMaterial IRON = register(IRON_KEY, Style.EMPTY.withColor(15527148), SuitMaterials.Palette.IRON);
    public static final SuitMaterial NETHERITE = register(NETHERITE_KEY, Style.EMPTY.withColor(6445145), SuitMaterials.Palette.NETHERITE);
    public static final SuitMaterial REDSTONE = register(REDSTONE_KEY, Style.EMPTY.withColor(9901575), SuitMaterials.Palette.REDSTONE);
    public static final SuitMaterial COPPER = register(COPPER_KEY, Style.EMPTY.withColor(11823181), SuitMaterials.Palette.COPPER);
    public static final SuitMaterial GOLD = register(GOLD_KEY, Style.EMPTY.withColor(14594349), SuitMaterials.Palette.GOLD);
    public static final SuitMaterial EMERALD = register(EMERALD_KEY, Style.EMPTY.withColor(1155126), SuitMaterials.Palette.EMERALD);
    public static final SuitMaterial DIAMOND = register(DIAMOND_KEY, Style.EMPTY.withColor(7269586), SuitMaterials.Palette.DIAMOND);
    public static final SuitMaterial LAPIS = register(LAPIS_KEY, Style.EMPTY.withColor(4288151), SuitMaterials.Palette.LAPIS);
    public static final SuitMaterial AMETHYST = register(AMETHYST_KEY, Style.EMPTY.withColor(10116294), SuitMaterials.Palette.AMETHYST);
    public static final SuitMaterial RESIN = register(RESIN_KEY, Style.EMPTY.withColor(16545810), SuitMaterials.Palette.RESIN);


    private static SuitMaterial register(final ResourceKey<SuitMaterial> registryKey, final Style hoverTextStyle, final SuitMaterials.Palette palette) {

        Component description = Component.translatable(Util.makeDescriptionId("suit_material", registryKey.identifier())).withStyle(hoverTextStyle);
        return Registry.register(ModRegistries.suitMaterials, registryKey, new SuitMaterial(palette.id(), description));
    }

    private static ResourceKey<SuitMaterial> registryKey(final String id) {
        return ResourceKey.create(ModRegistries.SUIT_MATERIAL, Identifier.fromNamespaceAndPath(Project_vearth.MOD_ID, id));
    }
    public enum Palette {
        QUARTZ("quartz"),
        IRON("iron"),
        IRON_DARKER("iron_darker"),
        NETHERITE("netherite"),
        NETHERITE_DARKER("netherite_darker"),
        REDSTONE("redstone"),
        COPPER("copper"),
        COPPER_DARKER("copper_darker"),
        GOLD("gold"),
        GOLD_DARKER("gold_darker"),
        EMERALD("emerald"),
        DIAMOND("diamond"),
        DIAMOND_DARKER("diamond_darker"),
        LAPIS("lapis"),
        AMETHYST("amethyst"),
        RESIN("resin");

        private final String suffix;
        private final Identifier id;

        Palette(final String name) {
            this.suffix = name;
            this.id = Identifier.fromNamespaceAndPath(Project_vearth.MOD_ID, "suit/" + name);
        }

        public String suffix() {
            return this.suffix;
        }

        public Identifier id() {
            return this.id;
        }
    }

    public static void initialize() {}
}
