package vance.vearth.item.equipment;

import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import vance.vearth.Project_vearth;

public interface ModEquipmentAssetKeys {
    RegistryKey<? extends Registry<EquipmentAsset>> REGISTRY_KEY = RegistryKey.ofRegistry(Identifier.ofVanilla("equipment_asset"));
    RegistryKey<EquipmentAsset> SPACE_SUIT = register("space_suit");

    static RegistryKey<EquipmentAsset> register(String name) {
        return RegistryKey.of(REGISTRY_KEY, Identifier.of(Project_vearth.MOD_ID, name));
    }

}
