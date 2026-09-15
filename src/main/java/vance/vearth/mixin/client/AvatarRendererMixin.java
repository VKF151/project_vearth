package vance.vearth.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.palette.PalettedTextureManager;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vance.vearth.components.ModComponents;
import vance.vearth.resources.util.ArmorLayerFirstPersonRenderer;
import vance.vearth.world.item.equipment.spaceSuit.SpaceSuit;

@Environment(EnvType.CLIENT)
@Mixin(AvatarRenderer.class)
public class AvatarRendererMixin{

    public AvatarRendererMixin() {
        super();
    }

    @Inject(method = "renderHand", at = @At("HEAD"), cancellable = true)
    private void renderSuitHand(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, Identifier skinTexture, ModelPart arm, boolean hasSleeve, CallbackInfo ci) {
        LocalPlayer player = Minecraft.getInstance().player;
        AvatarRenderer instance = (AvatarRenderer) (Object) this;
        if (player != null) {
            PlayerModel model = (PlayerModel) instance.getModel();
            arm.resetPose();
            arm.visible = true;
            model.leftSleeve.visible = hasSleeve;
            model.rightSleeve.visible = hasSleeve;
            model.leftArm.zRot = -0.1F;
            model.rightArm.zRot = 0.1F;
            ItemStack stack = player.getItemBySlot(EquipmentSlot.CHEST);
            SpaceSuit suit = stack.get(ModComponents.SUIT);
            if (suit != null) {
                ArmorLayerFirstPersonRenderer firstPersonRenderer = new ArmorLayerFirstPersonRenderer(Minecraft.getInstance().getPalettedTextureManager());
                PalettedTextureManager.Handle suitTextureHandler = firstPersonRenderer.getSuitArmTexture(stack);
                if (suitTextureHandler != null) {
                    submitNodeCollector.submitModelPart(arm, poseStack, RenderTypes.entityTranslucent(suitTextureHandler.textureLocation()), lightCoords, OverlayTexture.NO_OVERLAY, suitTextureHandler);
                    ci.cancel();
                }

            }
        }
    }
}
