package vance.vearth.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vance.vearth.Project_vearthClient;
import vance.vearth.client.renderer.entity.armor.SpaceSuitTankRenderer;
import vance.vearth.client.renderer.entity.armor.SpaceSuitUnderArmorRenderer;
import vance.vearth.components.ModComponents;
import vance.vearth.client.LayerRenderState;

import java.util.*;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<S extends HumanoidRenderState, M extends HumanoidModel<S>> extends RenderLayer<S, M> {

    public HumanoidArmorLayerMixin(RenderLayerParent<S, M> renderer) {
        super(renderer);
    }



    @Inject(method = "renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At("TAIL"))
    private void renderArmorPiece(
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            ItemStack itemStack,
            EquipmentSlot slot,
            int lightCoords,
            S state,
            CallbackInfo ci
    ) {
            M model = getParentModel();
            LayerRenderState<S, M> layer = new LayerRenderState<>();
            layer.model = model;
            layer.state = state;
            layer.light = lightCoords;
        if (itemStack.has(ModComponents.SUIT)) {
            Equippable equippable = itemStack.get(DataComponents.EQUIPPABLE);
            EntityModelSet entityModelSet = Minecraft.getInstance().getEntityModels();
            AtlasManager atlasManager = Minecraft.getInstance().getAtlasManager();
            TextureAtlas textureAtlas = atlasManager.getAtlasOrThrow(Project_vearthClient.SUIT_ATLAS_KEY);

            SpaceSuitUnderArmorRenderer spaceSuitUnderArmorRenderer = new SpaceSuitUnderArmorRenderer(entityModelSet, slot, textureAtlas);
            SpaceSuitTankRenderer spaceSuitTankRenderer = new SpaceSuitTankRenderer(entityModelSet, slot, textureAtlas);

            assert equippable != null;
            spaceSuitUnderArmorRenderer.renderSuit(poseStack, submitNodeCollector, itemStack, state, lightCoords, (HumanoidModel<HumanoidRenderState>) model, EquipmentClientInfo.LayerType.HUMANOID, equippable.assetId().orElseThrow());
            spaceSuitTankRenderer.renderTank(poseStack, submitNodeCollector, itemStack, state, lightCoords, (HumanoidModel<HumanoidRenderState>) model, EquipmentClientInfo.LayerType.HUMANOID, equippable.assetId().orElseThrow(), hasCape(state));
        } else if (itemStack.has(ModComponents.ARMOR_LAYER)) {
                submitNodeCollector.order(0).submitCustomGeometry(poseStack, RenderTypes.armorCutoutNoCull(Objects.requireNonNull(itemStack.get(ModComponents.ARMOR_LAYER))), layer);
        }

    }

    @Unique
    private static boolean hasCape(final HumanoidRenderState state) {
        if (state instanceof AvatarRenderState playerState) {
            PlayerSkin skin = playerState.skin;
            return (skin.cape() != null && playerState.showCape);
        }
        return false;
    }
}
