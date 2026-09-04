package vance.vearth.client.renderer.entity.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import vance.vearth.Project_vearth;
import vance.vearth.client.renderer.entity.armor.model.SpaceSuitArmorModel;

public record SpaceSuitArmorRenderer(SpaceSuitArmorModel<HumanoidRenderState> armorModel) implements ArmorRenderer {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Project_vearth.MOD_ID, "textures/entity/equipment/humanoid/space_suit_full.png");

    public SpaceSuitArmorRenderer(EntityRendererProvider.Context context, EquipmentSlot slot) {
        this(new SpaceSuitArmorModel<>(context.bakeLayer(SpaceSuitArmorModel.MODEL_LAYERS.get(slot))));
    }

    @Override
    public void render(@NonNull PoseStack poseStack, SubmitNodeCollector submitNodeCollector, ItemStack stack, @NonNull HumanoidRenderState humanoidRenderState, @NonNull EquipmentSlot slot, int light, @NonNull HumanoidModel<HumanoidRenderState> contextModel) {
        OrderedSubmitNodeCollector queue = submitNodeCollector.order(1);
        ArmorRenderer.submitTransformCopyingModel(contextModel, humanoidRenderState, armorModel, humanoidRenderState, false, queue, poseStack, RenderTypes.armorTranslucent(TEXTURE), light, OverlayTexture.NO_OVERLAY, humanoidRenderState.outlineColor, null);
        if (stack.hasFoil()) {
            ArmorRenderer.submitTransformCopyingModel(contextModel, humanoidRenderState, armorModel, humanoidRenderState, false, queue, poseStack, RenderTypes.armorEntityGlint(), light, OverlayTexture.NO_OVERLAY, humanoidRenderState.outlineColor, null);
        }



    }
}
