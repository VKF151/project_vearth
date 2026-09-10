package vance.vearth.client.renderer.entity.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentAsset;
import org.jspecify.annotations.NonNull;
import vance.vearth.Project_vearth;
import vance.vearth.Project_vearthClient;
import vance.vearth.client.renderer.entity.armor.model.SpaceSuitUnderArmorModel;
import vance.vearth.components.ModComponents;
import vance.vearth.world.item.equipment.spaceSuit.SpaceSuit;

public record SpaceSuitUnderArmorRenderer(SpaceSuitUnderArmorModel<HumanoidRenderState> armorModel, TextureAtlas suitAtlas) implements ArmorRenderer {

    public static final Identifier SPACE_SUIT_SHEET = Identifier.fromNamespaceAndPath(Project_vearth.MOD_ID,  "textures/atlas/space_suits.png");

    public SpaceSuitUnderArmorRenderer(EntityModelSet entityModelSet, EquipmentSlot slot, final TextureAtlas suitAtlas) {
        this(new SpaceSuitUnderArmorModel<>(entityModelSet.bakeLayer(SpaceSuitUnderArmorModel.MODEL_LAYERS.get(slot))), suitAtlas);
    }

    public void renderSuit(
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            ItemStack stack,
            HumanoidRenderState humanoidRenderState,
            int light,
            HumanoidModel<HumanoidRenderState> contextModel,
            final EquipmentClientInfo.LayerType layerType,
            final ResourceKey<EquipmentAsset> equipmentAssetId) {
        SpaceSuit suit = stack.get(ModComponents.SUIT);
        if (suit != null) {
            TextureAtlasSprite sprite = getAtlasSprite(suit, layerType, equipmentAssetId);
            OrderedSubmitNodeCollector queue = submitNodeCollector.order(1);
            ArmorRenderer.submitTransformCopyingModel(contextModel, humanoidRenderState, armorModel, humanoidRenderState, true, queue, poseStack, RenderTypes.armorTranslucent(sprite.atlasLocation()), light, OverlayTexture.NO_OVERLAY, -1, sprite, humanoidRenderState.outlineColor, null);
            if (stack.hasFoil()) {
                ArmorRenderer.submitTransformCopyingModel(contextModel, humanoidRenderState, armorModel, humanoidRenderState, true, queue, poseStack, RenderTypes.armorEntityGlint(), light, OverlayTexture.NO_OVERLAY, -1, sprite, humanoidRenderState.outlineColor, null);
            }
        }


    }

    public static String suitAssetPrefix(String id) {
        return "entity/equipment/" + id;
    }

    public static TextureAtlasSprite getAtlasSprite(SpaceSuit suit, EquipmentClientInfo.LayerType layerType, ResourceKey<EquipmentAsset> equipmentAssetId) {
        TextureAtlas suitAtlas = Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(Project_vearthClient.SUIT_ATLAS_KEY);
        Identifier spritePath = suit.layerAssetId(suitAssetPrefix(layerType.getSerializedName()), equipmentAssetId, false);
        return suitAtlas.getSprite(spritePath);

    }

    @Override
    public void render(@NonNull PoseStack poseStack, @NonNull SubmitNodeCollector submitNodeCollector, @NonNull ItemStack stack, @NonNull HumanoidRenderState humanoidRenderState, @NonNull EquipmentSlot slot, int light, @NonNull HumanoidModel<HumanoidRenderState> contextModel) {

    }
}
