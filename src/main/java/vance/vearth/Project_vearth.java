package vance.vearth;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.world.poi.PoiHelper;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vance.vearth.block.ModBlocks;
import vance.vearth.components.ModComponents;
import vance.vearth.item.ModItems;

public class Project_vearth implements ModInitializer {
	public static final String MOD_ID = "project_vearth";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModBlocks.initialize();
		ModComponents.initialize();
		PoiHelper.register(Identifier.fromNamespaceAndPath(MOD_ID, "vearth_portal"), 0, 1, ModBlocks.VEARTH_PORTAL);
	}
}