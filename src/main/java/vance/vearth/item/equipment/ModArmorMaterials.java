package vance.vearth.item.equipment;

import com.google.common.collect.Maps;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.tags.ItemTags;
import net.minecraft.sounds.SoundEvents;

import java.util.Map;

public interface ModArmorMaterials {

    ArmorMaterial SPACE_SUIT = new ArmorMaterial(15, createDefenseMap(2, 5, 6, 2, 5), 9, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F, ItemTags.REPAIRS_IRON_ARMOR, ModEquipmentAssetKeys.SPACE_SUIT);


    private static Map<ArmorType, Integer> createDefenseMap(int bootsDefense, int leggingsDefense, int chestplateDefense, int helmetDefense, int bodyDefense) {
        return Maps.newEnumMap(Map.of(ArmorType.BOOTS, bootsDefense, ArmorType.LEGGINGS, leggingsDefense, ArmorType.CHESTPLATE, chestplateDefense, ArmorType.HELMET, helmetDefense, ArmorType.BODY, bodyDefense));
    }

}
