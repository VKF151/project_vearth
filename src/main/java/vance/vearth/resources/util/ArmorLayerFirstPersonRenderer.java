package vance.vearth.resources.util;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import org.jspecify.annotations.Nullable;
import vance.vearth.components.ModComponents;

public class ArmorLayerFirstPersonRenderer {
    @Nullable
    public static Identifier getHandTexture(@Nullable LocalPlayer player) {
        if (player != null && player.getItemBySlot(EquipmentSlot.CHEST).has(ModComponents.ARMOR_LAYER)) {
            return player.getItemBySlot(EquipmentSlot.CHEST).get(ModComponents.ARMOR_LAYER);
        }
        return null;
    }
}
