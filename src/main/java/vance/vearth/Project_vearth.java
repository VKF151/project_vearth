package vance.vearth;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vance.vearth.LifeSupport.LifeSupportHandler;
import vance.vearth.block.ModBlocks;
import vance.vearth.components.ModComponents;
import vance.vearth.gravity.GravityHandler;
import vance.vearth.item.ModItems;

public class Project_vearth implements ModInitializer {
	public static final String MOD_ID = "project_vearth";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModBlocks.initialize();
		GravityHandler.register();
		LifeSupportHandler.register();
		ModComponents.initialize();
	}
}