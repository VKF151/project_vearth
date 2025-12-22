package vance.vearth.item.equipment;

import com.google.common.collect.Maps;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentAssetKeys;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.SoundEvents;

import java.util.Map;

public interface ModArmorMaterials {

    ArmorMaterial SPACE_SUIT = new ArmorMaterial(15, createDefenseMap(2, 5, 6, 2, 5), 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F, 0.0F, ItemTags.REPAIRS_IRON_ARMOR, ModEquipmentAssetKeys.SPACE_SUIT);


    private static Map<EquipmentType, Integer> createDefenseMap(int bootsDefense, int leggingsDefense, int chestplateDefense, int helmetDefense, int bodyDefense) {
        return Maps.newEnumMap(Map.of(EquipmentType.BOOTS, bootsDefense, EquipmentType.LEGGINGS, leggingsDefense, EquipmentType.CHESTPLATE, chestplateDefense, EquipmentType.HELMET, helmetDefense, EquipmentType.BODY, bodyDefense));
    }

}
