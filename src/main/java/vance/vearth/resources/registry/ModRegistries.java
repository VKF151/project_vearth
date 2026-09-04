package vance.vearth.resources.registry;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import vance.vearth.Project_vearth;
import vance.vearth.world.item.equipment.spaceSuit.SuitDesign;
import vance.vearth.world.item.equipment.spaceSuit.SuitMaterial;

import static net.minecraft.resources.ResourceKey.createRegistryKey;

public class ModRegistries {
    public static final ResourceKey<Registry<SuitDesign>> SUIT_DESIGN = createRegistryKey(Identifier.fromNamespaceAndPath(Project_vearth.MOD_ID, "suit_design"));
    public static final ResourceKey<Registry<SuitMaterial>> SUIT_MATERIAL = createRegistryKey(Identifier.fromNamespaceAndPath(Project_vearth.MOD_ID, "suit_material"));

    public static final Registry<SuitDesign> suitDesigns = FabricRegistryBuilder.create(SUIT_DESIGN).attribute(RegistryAttribute.SYNCED).buildAndRegister();
    public static final Registry<SuitMaterial> suitMaterials = FabricRegistryBuilder.create(SUIT_MATERIAL).attribute(RegistryAttribute.SYNCED).buildAndRegister();

    public static void initialize() {}

}
