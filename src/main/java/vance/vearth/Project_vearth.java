package vance.vearth;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.world.poi.PoiHelper;
import net.fabricmc.fabric.api.recipe.v1.sync.RecipeSynchronization;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vance.vearth.block.ModBlocks;
import vance.vearth.components.ModComponents;
import vance.vearth.gravity.GravityHandler;
import vance.vearth.item.ModItems;
import vance.vearth.world.item.crafting.SmithingInsulationRecipe;
import vance.vearth.world.item.crafting.SmithingMembraneRecipe;

public class Project_vearth implements ModInitializer {
	public static final String MOD_ID = "project_vearth";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModBlocks.initialize();
		ModComponents.initialize();
		GravityHandler.register();
		PoiHelper.register(Identifier.fromNamespaceAndPath(MOD_ID, "vearth_portal"), 0, 1, ModBlocks.OPEN_ECHOFLOWER);
		RecipeSynchronization.synchronizeRecipeSerializer(MEMBRANING_RECIPE_SERIALIZER);
		RecipeSynchronization.synchronizeRecipeSerializer(INSULATING_RECIPE_SERIALIZER);
	}
	public static final RecipeSerializer<SmithingMembraneRecipe> MEMBRANING_RECIPE_SERIALIZER = Registry.register(
			BuiltInRegistries.RECIPE_SERIALIZER,
			Identifier.fromNamespaceAndPath(MOD_ID, "membraning"),
			new RecipeSerializer<>(SmithingMembraneRecipe.MAP_CODEC, SmithingMembraneRecipe.STREAM_CODEC)
	);
	public static final RecipeType<SmithingMembraneRecipe> MEMBRANING_RECIPE_TYPE = Registry.register(
			BuiltInRegistries.RECIPE_TYPE,
			Identifier.fromNamespaceAndPath(MOD_ID, "membraning"),
			new RecipeType<SmithingMembraneRecipe>() {}
	);
	public static final RecipeSerializer<SmithingInsulationRecipe> INSULATING_RECIPE_SERIALIZER = Registry.register(
			BuiltInRegistries.RECIPE_SERIALIZER,
			Identifier.fromNamespaceAndPath(MOD_ID, "insulating"),
			new RecipeSerializer<>(SmithingInsulationRecipe.MAP_CODEC, SmithingInsulationRecipe.STREAM_CODEC)
	);
	public static final RecipeType<SmithingInsulationRecipe> INSULATING_RECIPE_TYPE = Registry.register(
			BuiltInRegistries.RECIPE_TYPE,
			Identifier.fromNamespaceAndPath(MOD_ID, "insulating"),
			new RecipeType<SmithingInsulationRecipe>() {}
	);
}