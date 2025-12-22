package vance.vearth.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import vance.vearth.Project_vearth;
import vance.vearth.components.ModComponents;
import vance.vearth.item.equipment.ModArmorMaterials;

public class ModItems {

    public static final Item MOD_ICON_ITEM = registerItem("mod_icon_item", new Item(
            new Item.Settings()
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Project_vearth.MOD_ID, "mod_icon_item")))
            ));

    public static final Item SPACE_SUIT_HELMET = registerItem("space_suit_helmet", new Item(
            new Item.Settings()
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Project_vearth.MOD_ID, "space_suit_helmet")))
                    .armor(ModArmorMaterials.SPACE_SUIT , EquipmentType.HELMET)
            ));

    public static final Item SPACE_SUIT_CHESTPLATE = registerItem("space_suit_chestplate", new Item(
            new Item.Settings()
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Project_vearth.MOD_ID, "space_suit_chestplate")))
                    .armor(ModArmorMaterials.SPACE_SUIT, EquipmentType.CHESTPLATE).component(ModComponents.OXYGEN_STORAGE, 1)
            ));

    public static final Item SPACE_SUIT_LEGGINGS = registerItem("space_suit_leggings", new Item(
            new Item.Settings()
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Project_vearth.MOD_ID, "space_suit_leggings")))
                    .armor(ModArmorMaterials.SPACE_SUIT, EquipmentType.LEGGINGS)
            ));

    public static final Item SPACE_SUIT_BOOTS = registerItem("space_suit_boots", new Item(
            new Item.Settings()
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Project_vearth.MOD_ID, "space_suit_boots")))
                    .armor(ModArmorMaterials.SPACE_SUIT, EquipmentType.BOOTS)
            ));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Project_vearth.MOD_ID, name), item);
    }

    public static final RegistryKey<ItemGroup> PROJECT_VEARTH_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(),
            Identifier.of(Project_vearth.MOD_ID, "project_vearth_group"));

    public static final ItemGroup PROJECT_VEARTH_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.MOD_ICON_ITEM))
            .displayName(Text.translatable("itemgroup.project_vearth"))
            .build();


    public static void registerModItems() {
        Project_vearth.LOGGER.info("registering items for " + Project_vearth.MOD_ID);

        Registry.register(Registries.ITEM_GROUP, PROJECT_VEARTH_GROUP_KEY, PROJECT_VEARTH_GROUP);

        ItemGroupEvents.modifyEntriesEvent(PROJECT_VEARTH_GROUP_KEY).register(fabricItemGroupEntries -> {
            fabricItemGroupEntries.add(ModItems.MOD_ICON_ITEM);
            fabricItemGroupEntries.add(ModItems.SPACE_SUIT_HELMET);
            fabricItemGroupEntries.add(ModItems.SPACE_SUIT_CHESTPLATE);
            fabricItemGroupEntries.add(ModItems.SPACE_SUIT_LEGGINGS);
            fabricItemGroupEntries.add(ModItems.SPACE_SUIT_BOOTS);
        });
    }

}
