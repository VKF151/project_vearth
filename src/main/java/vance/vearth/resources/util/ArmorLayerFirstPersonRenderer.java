package vance.vearth.resources.util;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.client.resources.palette.PalettedTextureManager;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import vance.vearth.Project_vearth;
import vance.vearth.client.renderer.entity.armor.MaterialSuitRenderer;
import vance.vearth.components.ModComponents;
import vance.vearth.world.item.equipment.spaceSuit.SpaceSuit;

import java.util.function.Function;

public class ArmorLayerFirstPersonRenderer {
    private static final String LAYER_PATH = "textures/entity/equipment/humanoid_under/";
    private static final Identifier ARMS = Identifier.fromNamespaceAndPath(Project_vearth.MOD_ID, LAYER_PATH + "1st_person_arms.png");

    private final Function<MaterialSuitRenderer.SuitTextureKey, PalettedTextureManager.Handle> suitTextureLookup;

    public ArmorLayerFirstPersonRenderer(final PalettedTextureManager palettedTextures) {
        this.suitTextureLookup = Util.memoize((key) -> key.getOrPrepareTexture(palettedTextures));
    }

    @Nullable
    public static Identifier getHandTexture(@Nullable LocalPlayer player) {
        if (player != null) {
            if (player.getItemBySlot(EquipmentSlot.CHEST).has(ModComponents.ARMOR_LAYER)) {
                return player.getItemBySlot(EquipmentSlot.CHEST).get(ModComponents.ARMOR_LAYER);
            } else if (player.getItemBySlot(EquipmentSlot.CHEST).has(ModComponents.SUIT)) {
                return ARMS;
            }
        }
        return null;
    }

    public PalettedTextureManager.@Nullable Handle getSuitArmTexture(final ItemStack stack) {
        if (stack.has(ModComponents.SUIT)) {
            SpaceSuit suit = stack.get(ModComponents.SUIT);
            boolean hasSuit = suit != null;
            if (hasSuit) {

                return (PalettedTextureManager.Handle) this.suitTextureLookup.apply(new MaterialSuitRenderer.SuitTextureKey(suit, EquipmentClientInfo.LayerType.HUMANOID, false));
            }
        }
        return null;
    }
}
