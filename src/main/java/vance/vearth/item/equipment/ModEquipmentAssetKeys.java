package vance.vearth.item.equipment;

import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import vance.vearth.Project_vearth;

public interface ModEquipmentAssetKeys {
    ResourceKey<? extends Registry<EquipmentAsset>> REGISTRY_KEY = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("equipment_asset"));
    ResourceKey<EquipmentAsset> SPACE_SUIT = register("space_suit");

    static ResourceKey<EquipmentAsset> register(String name) {
        return ResourceKey.create(REGISTRY_KEY, Identifier.fromNamespaceAndPath(Project_vearth.MOD_ID, name));
    }

}
