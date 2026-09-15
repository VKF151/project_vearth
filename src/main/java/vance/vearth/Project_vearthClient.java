package vance.vearth;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import vance.vearth.client.renderer.entity.armor.model.SpaceSuitArmorModel;
import vance.vearth.client.renderer.entity.armor.model.SpaceSuitTankModel;
import vance.vearth.client.renderer.entity.armor.model.SpaceSuitUnderArmorModel;
import vance.vearth.components.ModComponents;

public class Project_vearthClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        initItems();
        initEntities();
        LivingEntityFeatureRenderEvents.ALLOW_CAPE_RENDER.register((_) -> {
            Player localPlayer = Minecraft.getInstance().player;
            return localPlayer == null || !localPlayer.getItemBySlot(EquipmentSlot.CHEST).has(ModComponents.SUIT);
        });

        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT, Identifier.fromNamespaceAndPath(Project_vearth.MOD_ID, "timer_before_chat"), this.timer());

    }

    private void initItems() {
    }

    private void initEntities() {
        ModelLayerRegistry.registerArmorModelLayers(SpaceSuitArmorModel.MODEL_LAYERS, SpaceSuitArmorModel::createArmorMeshSet);
        ModelLayerRegistry.registerArmorModelLayers(SpaceSuitUnderArmorModel.MODEL_LAYERS, SpaceSuitUnderArmorModel::createArmorMeshSet);
        ModelLayerRegistry.registerArmorModelLayers(SpaceSuitTankModel.MODEL_LAYERS, SpaceSuitTankModel::createArmorMeshSet);
    }

    private HudElement timer() {
        return (graphics, deltaTracker) -> {
                Player player = Minecraft.getInstance().player;
                if (player == null || !player.getItemBySlot(EquipmentSlot.CHEST).has(ModComponents.RESPIRANT_STORAGE)) {
                    return;
                }

                int color = 0xFFFFFFFF;
                int guiWidth = graphics.guiWidth();
                int guiHeight = graphics.guiHeight();
                int screenCenter = guiWidth / 2;

                int currentRespirant = player.getItemBySlot(EquipmentSlot.CHEST).get(ModComponents.RESPIRANT_STORAGE).respirantAmount();

                int respirantSecsRemaining = (currentRespirant/20);

                int respirantMinsRemaining = respirantSecsRemaining / 60;
                int respirantMinSecs = respirantSecsRemaining % 60;
                String text = String.format("%d:%02d", respirantMinsRemaining, respirantMinSecs);

                graphics.text(Minecraft.getInstance().font, text, screenCenter - 120, guiHeight - 15, color, false);
        };
    }

}
