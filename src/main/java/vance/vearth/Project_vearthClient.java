package vance.vearth;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.AtlasRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import vance.vearth.client.renderer.entity.armor.SpaceSuitArmorRenderer;
import vance.vearth.client.renderer.entity.armor.SpaceSuitUnderArmorRenderer;
import vance.vearth.client.renderer.entity.armor.model.SpaceSuitArmorModel;
import vance.vearth.client.renderer.entity.armor.model.SpaceSuitUnderArmorModel;
import vance.vearth.item.ModItems;

public class Project_vearthClient implements ClientModInitializer {
    public static final Identifier SUIT_ATLAS_KEY = Identifier.fromNamespaceAndPath("project_vearth", "space_suits");

    @Override
    public void onInitializeClient() {
        initItems();
        initEntities();

        AtlasManager.AtlasConfig config = new AtlasManager.AtlasConfig(
                SpaceSuitUnderArmorRenderer.SPACE_SUIT_SHEET,
                SUIT_ATLAS_KEY,
                false
        );
        AtlasRegistry.register(config);
    }

    private void initItems() {
        ArmorRenderer.register(context -> new SpaceSuitArmorRenderer(context, EquipmentSlot.HEAD), ModItems.SPACE_SUIT_HELMET);
        ArmorRenderer.register(context -> new SpaceSuitArmorRenderer(context, EquipmentSlot.CHEST), ModItems.SPACE_SUIT_CHESTPLATE);
        ArmorRenderer.register(context -> new SpaceSuitArmorRenderer(context, EquipmentSlot.LEGS), ModItems.SPACE_SUIT_LEGGINGS);
        ArmorRenderer.register(context -> new SpaceSuitArmorRenderer(context, EquipmentSlot.FEET), ModItems.SPACE_SUIT_BOOTS);
    }

    private void initEntities() {
        ModelLayerRegistry.registerArmorModelLayers(SpaceSuitArmorModel.MODEL_LAYERS, SpaceSuitArmorModel::createArmorMeshSet);
        ModelLayerRegistry.registerArmorModelLayers(SpaceSuitUnderArmorModel.MODEL_LAYERS, SpaceSuitUnderArmorModel::createArmorMeshSet);
    }

}
