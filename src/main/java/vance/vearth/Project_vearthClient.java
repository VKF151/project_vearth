package vance.vearth;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.AtlasRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import vance.vearth.client.renderer.entity.armor.SpaceSuitTankRenderer;
import vance.vearth.client.renderer.entity.armor.SpaceSuitUnderArmorRenderer;
import vance.vearth.client.renderer.entity.armor.model.SpaceSuitArmorModel;
import vance.vearth.client.renderer.entity.armor.model.SpaceSuitTankModel;
import vance.vearth.client.renderer.entity.armor.model.SpaceSuitUnderArmorModel;
import vance.vearth.components.ModComponents;

public class Project_vearthClient implements ClientModInitializer {
    public static final Identifier SUIT_ATLAS_KEY = Identifier.fromNamespaceAndPath("project_vearth", "space_suits");
    public static final Identifier TANK_ATLAS_KEY = Identifier.fromNamespaceAndPath("project_vearth", "space_suit_tanks");

    @Override
    public void onInitializeClient() {
        initItems();
        initEntities();

        AtlasManager.AtlasConfig suits = new AtlasManager.AtlasConfig(
                SpaceSuitUnderArmorRenderer.SPACE_SUIT_SHEET,
                SUIT_ATLAS_KEY,
                false
        );AtlasManager.AtlasConfig suit_tanks = new AtlasManager.AtlasConfig(
                SpaceSuitTankRenderer.SPACE_SUIT_TANK_SHEET,
                TANK_ATLAS_KEY,
                false
        );
        AtlasRegistry.register(suits);
        AtlasRegistry.register(suit_tanks);
        LivingEntityFeatureRenderEvents.ALLOW_CAPE_RENDER.register((_) -> {
            Player localPlayer = Minecraft.getInstance().player;
            return localPlayer == null || !localPlayer.getItemBySlot(EquipmentSlot.CHEST).has(ModComponents.SUIT);
        });
    }

    private void initItems() {
    }

    private void initEntities() {
        ModelLayerRegistry.registerArmorModelLayers(SpaceSuitArmorModel.MODEL_LAYERS, SpaceSuitArmorModel::createArmorMeshSet);
        ModelLayerRegistry.registerArmorModelLayers(SpaceSuitUnderArmorModel.MODEL_LAYERS, SpaceSuitUnderArmorModel::createArmorMeshSet);
        ModelLayerRegistry.registerArmorModelLayers(SpaceSuitTankModel.MODEL_LAYERS, SpaceSuitTankModel::createArmorMeshSet);
    }

}
