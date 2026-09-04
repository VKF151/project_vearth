package vance.vearth.resources.util;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import org.jspecify.annotations.Nullable;
import vance.vearth.Project_vearth;
import vance.vearth.components.ModComponents;

public class ArmorLayerFirstPersonRenderer {
    private static final String LAYER_PATH = "textures/entity/equipment/humanoid_under/";
    private static final Identifier ARMS = Identifier.fromNamespaceAndPath(Project_vearth.MOD_ID, LAYER_PATH + "1st_person_arms.png");
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
}
