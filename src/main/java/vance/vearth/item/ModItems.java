package vance.vearth.item;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import vance.vearth.Project_vearth;
import vance.vearth.block.ModBlocks;
import vance.vearth.components.ModComponents;
import vance.vearth.item.equipment.ModArmorMaterials;
import vance.vearth.resources.Identifier.ModBlockItemId;
import vance.vearth.resources.Identifier.ModBlockItemIds;
import vance.vearth.resources.Identifier.ModItemIds;

import java.util.function.BiFunction;
import java.util.function.Function;

public class ModItems {
    private static final String LAYER_PATH = "textures/entity/equipment/humanoid_under/";
    private static final Identifier SPACE_SUIT_LAYER = Identifier.fromNamespaceAndPath(Project_vearth.MOD_ID, LAYER_PATH + "space_suit.png");

    public static final Item MOD_ICON_ITEM = registerItem(ModItemIds.MOD_ICON,
            (new Item.Properties())
    );

    public static final Item SPACE_SUIT_HELMET = registerItem(ModItemIds.SPACE_SUIT_HELMET,
            (new Item.Properties()
                    .humanoidArmor(ModArmorMaterials.SPACE_SUIT, ArmorType.HELMET)
                    .component(ModComponents.SEALED, true)
            ));

    public static final Item SPACE_SUIT_CHESTPLATE = registerItem(ModItemIds.SPACE_SUIT_CHESTPLATE,
            (new Item.Properties()
                    .humanoidArmor(ModArmorMaterials.SPACE_SUIT, ArmorType.CHESTPLATE)
                    .component(ModComponents.SEALED, true)
                    .component(ModComponents.OXYGEN_STORAGE, 0)
                    .component(ModComponents.ARMOR_LAYER, SPACE_SUIT_LAYER)
            ));

    public static final Item SPACE_SUIT_LEGGINGS = registerItem(ModItemIds.SPACE_SUIT_LEGGINGS,
            (new Item.Properties()
                    .humanoidArmor(ModArmorMaterials.SPACE_SUIT, ArmorType.LEGGINGS)
                    .component(ModComponents.SEALED, true)
            ));

    public static final Item SPACE_SUIT_BOOTS = registerItem(ModItemIds.SPACE_SUIT_BOOTS,
            (new Item.Properties()
                    .humanoidArmor(ModArmorMaterials.SPACE_SUIT, ArmorType.BOOTS)
                    .component(ModComponents.SEALED, true)
            ));

    public static final Item REGOLITH =registerBlock(ModBlockItemIds.REGOLITH, ModBlocks.REGOLITH);
    public static final Item OPEN_ECHOFLOWER =registerBlock(ModBlockItemIds.OPEN_ECHOFLOWER, ModBlocks.OPEN_ECHOFLOWER);
    public static final Item CLOSED_ECHOFLOWER =registerBlock(ModBlockItemIds.CLOSED_ECHOFLOWER, ModBlocks.CLOSED_ECHOFLOWER);

    private static Item registerBlock(final ModBlockItemId id, final Block block) {
        return registerBlock(id, block, BlockItem::new);
    }

    private static Item registerBlock(final ModBlockItemId id, final Block block, final BiFunction<Block, Item.Properties, Item> itemFactory) {
        return registerBlock(id, block, itemFactory, new Item.Properties());
    }

    private static Item registerBlock(final ModBlockItemId id, final Block block, final BiFunction<Block, Item.Properties, Item> itemFactory, final Item.Properties properties) {
        return registerItem(id.item(), (p) -> itemFactory.apply(block, p), properties.useBlockDescriptionPrefix().requiredFeatures(block.requiredFeatures()));
    }

    private static Item registerItem(final ResourceKey<Item> id, final Item.Properties properties) {
        return registerItem(id, Item::new, properties);
    }

    private static Item registerItem(final ResourceKey<Item> id, final Function<Item.Properties, Item> itemFactory, final Item.Properties properties) {
        Item item = itemFactory.apply(properties.setId(id));
        if (item instanceof BlockItem blockItem) {
            blockItem.registerBlocks(Item.BY_BLOCK, item);
        }

        return Registry.register(BuiltInRegistries.ITEM, id, item);
    }

    public static final ResourceKey<CreativeModeTab> PROJECT_VEARTH_GROUP_KEY = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            Identifier.fromNamespaceAndPath(Project_vearth.MOD_ID, "project_vearth_group"));

    public static final CreativeModeTab PROJECT_VEARTH_GROUP = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(ModItems.MOD_ICON_ITEM))
            .title(Component.translatable("itemgroup.project_vearth"))
            .build();


    public static void registerModItems() {
        Project_vearth.LOGGER.info("registering items for " + Project_vearth.MOD_ID);

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, PROJECT_VEARTH_GROUP_KEY, PROJECT_VEARTH_GROUP);

        CreativeModeTabEvents.modifyOutputEvent(PROJECT_VEARTH_GROUP_KEY).register(fabricItemGroupEntries -> {
            fabricItemGroupEntries.accept(ModItems.MOD_ICON_ITEM);
            fabricItemGroupEntries.accept(ModItems.SPACE_SUIT_HELMET);
            fabricItemGroupEntries.accept(ModItems.SPACE_SUIT_CHESTPLATE);
            fabricItemGroupEntries.accept(ModItems.SPACE_SUIT_LEGGINGS);
            fabricItemGroupEntries.accept(ModItems.SPACE_SUIT_BOOTS);
            fabricItemGroupEntries.accept(ModItems.REGOLITH);
            fabricItemGroupEntries.accept(ModItems.OPEN_ECHOFLOWER);
            fabricItemGroupEntries.accept(ModItems.CLOSED_ECHOFLOWER);
        });
    }

}
