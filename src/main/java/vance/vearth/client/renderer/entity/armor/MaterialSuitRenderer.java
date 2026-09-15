package vance.vearth.client.renderer.entity.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.TransformCopyingModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.client.resources.palette.PalettedTextureManager;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import vance.vearth.client.renderer.entity.armor.model.SpaceSuitTankModel;
import vance.vearth.client.renderer.entity.armor.model.SpaceSuitUnderArmorModel;
import vance.vearth.components.ModComponents;
import vance.vearth.world.item.equipment.spaceSuit.SpaceSuit;
import vance.vearth.world.item.equipment.spaceSuit.SuitDesign;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class MaterialSuitRenderer {
    private final Function<SuitTextureKey, PalettedTextureManager.Handle> suitTextureLookup;
    private final SpaceSuitUnderArmorModel<HumanoidRenderState> suitModel;
    private final SpaceSuitTankModel<HumanoidRenderState> tankModel;

    public MaterialSuitRenderer(final PalettedTextureManager palettedTextures, EntityModelSet entityModelSet, EquipmentSlot slot) {
        this.suitModel = new SpaceSuitUnderArmorModel<>(entityModelSet.bakeLayer(SpaceSuitUnderArmorModel.MODEL_LAYERS.get(slot)));
        this.tankModel = new SpaceSuitTankModel<>(entityModelSet.bakeLayer(SpaceSuitTankModel.MODEL_LAYERS.get(slot)));
        Function<LayerTextureKey, Identifier> layerTextureLookup = Util.memoize((key) -> key.layer.getTextureLocation(key.layerType));
        this.suitTextureLookup = Util.memoize((key) -> key.getOrPrepareTexture(palettedTextures));
    }

    public void renderSuit(
            final EquipmentClientInfo.LayerType layerType,
            final ResourceKey<EquipmentAsset> equipmentAssetId,
            final HumanoidModel<HumanoidRenderState> model,
            final HumanoidRenderState state,
            final ItemStack stack,
            final PoseStack poseStack,
            final SubmitNodeCollector submitNodeCollector,
            final int lightCoords,
            final int outlineColor,
            final int order,
            EquipmentSlot slot
    ) {
        if (stack.has(ModComponents.SUIT)) {
            boolean hasFoil = stack.hasFoil();
            SpaceSuit suit = stack.get(ModComponents.SUIT);
            boolean hasSuit = suit != null && layerType != EquipmentClientInfo.LayerType.HUMANOID_BABY;
            boolean renderShaderGlint = hasFoil && !hasSuit;
            int nextOrder = order;
            if (hasSuit) {
                PalettedTextureManager.Handle suitTextureHandle = (PalettedTextureManager.Handle) this.suitTextureLookup.apply(new SuitTextureKey(suit, layerType, false));
                PalettedTextureManager.Handle tankTextureHandle = (PalettedTextureManager.Handle) this.suitTextureLookup.apply(new SuitTextureKey(suit, layerType, true));

                Identifier tankTextureLocation;
                if (hasCape(state) && state instanceof AvatarRenderState playerState) {
                    PlayerSkin skin = playerState.skin;
                    assert skin.cape() != null;
                    tankTextureLocation = skin.cape().texturePath();
                } else {
                    tankTextureLocation = tankTextureHandle.textureLocation();
                }


                RenderType suitRenderType = RenderTypes.entityTranslucent(suitTextureHandle.textureLocation());
                RenderType tankRenderType = RenderTypes.armorCutoutNoCull(tankTextureLocation);
                submitTransformCopyingModel(model, state,  suitModel, state, true, submitNodeCollector, poseStack, suitRenderType, lightCoords, OverlayTexture.NO_OVERLAY, -1, suitTextureHandle, outlineColor);
                if (slot == EquipmentSlot.CHEST) {
                    submitTransformCopyingModel(model, state, tankModel, state, true, submitNodeCollector, poseStack, tankRenderType, lightCoords, OverlayTexture.NO_OVERLAY, -1, hasCape(state) ? null : tankTextureHandle, outlineColor);
                }

            }
        }
    }
    public static <S, D> void submitTransformCopyingModel(Model<? super S> sourceModel, S sourceModelState, Model<? super D> delegateModel, D delegateModelState, boolean setDelegateAngles, OrderedSubmitNodeCollector nodeCollector, PoseStack poseStack, RenderType renderType, int light, int overlay, int tintedColor, PalettedTextureManager.Handle handle, int outlineColor) {
        nodeCollector.submitModel(TransformCopyingModel.create(sourceModel, delegateModel, setDelegateAngles), Pair.of(sourceModelState, delegateModelState),
                poseStack,
                renderType, light, overlay, tintedColor, handle, outlineColor);
    }

    private static boolean hasCape(final HumanoidRenderState state) {
        if (state instanceof AvatarRenderState playerState) {
            PlayerSkin skin = playerState.skin;
            return (skin.cape() != null && playerState.showCape);
        }
        return false;
    }

    @Environment(EnvType.CLIENT)
    public record LayerTextureKey(EquipmentClientInfo.LayerType layerType, EquipmentClientInfo.Layer layer) {}

    @Environment(EnvType.CLIENT)
    public record SuitTextureKey(SpaceSuit suit, EquipmentClientInfo.LayerType layerType, boolean isTank) {
        public PalettedTextureManager.Handle getOrPrepareTexture(final PalettedTextureManager palettedTextures) {
            Identifier textureId = ((SuitDesign) this.suit.design().value()).assetId();
            Identifier paletteId = ((TrimMaterial) this.suit.material().value()).paletteId();

            Identifier baseTexture = textureId.withPath((path) -> {
                String prefix = "suits/entity/" + this.layerType.getSerializedName();
                return isTank ? prefix + "/" + path + "_tank" : prefix + "/" + path;
            });
            return paletteId == null ? createTextureWithNoPalette(baseTexture) : palettedTextures.getOrPrepare(baseTexture, paletteId);
        }

        public static PalettedTextureManager.Handle createTextureWithNoPalette(final Identifier texture) {
            final Identifier textureLocation = texture.withPath((path) -> "textures/" + path + ".png");
            return new PalettedTextureManager.Handle() {
                @Override
                public Identifier textureLocation() {
                    return textureLocation;
                }

                @Override
                public float getU(float offset) {
                    return offset;
                }

                @Override
                public float getV(float offset) {
                    return offset;
                }
            };
        }
    }
}
