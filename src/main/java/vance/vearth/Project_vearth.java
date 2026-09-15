package vance.vearth;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PoiHelper;
import net.fabricmc.fabric.api.recipe.v1.sync.RecipeSynchronization;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vance.vearth.block.ModBlocks;
import vance.vearth.components.ModComponents;
import vance.vearth.gravity.GravityHandler;
import vance.vearth.item.ModItems;
import vance.vearth.resources.registry.ModRegistries;
import vance.vearth.world.ModGameRules;
import vance.vearth.world.effects.ModMobEffects;
import vance.vearth.world.item.crafting.SmithingSuitRecipe;
import vance.vearth.world.item.equipment.spaceSuit.SuitDesigns;
import vance.vearth.world.item.equipment.spaceSuit.SuitMaterials;
import vance.vearth.world.level.levelgen.feature.ModFeatures;

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
		RecipeSynchronization.synchronizeRecipeSerializer(SMITHING_SUIT_RECIPE_SERIALIZER);
		ModRegistries.initialize();
		SuitDesigns.initialize();
		SuitMaterials.initialize();
		ModFeatures.initialize();
		ModMobEffects.initialize();
		ModGameRules.initialize();

		ItemTooltipCallback.EVENT.register((stack, tooltipContext, tooltipFlag, lines) -> {
			if (stack.has(ModComponents.RESPIRANT_STORAGE)) {
				int storedOxygen = stack.get(ModComponents.RESPIRANT_STORAGE).respirantAmount();
				int maxOxygen = stack.get(ModComponents.RESPIRANT_STORAGE).maxRespirantAmount();
				lines.add(Component.literal("Oxygen: " + storedOxygen + " / " + maxOxygen).withColor(TextColor.WHITE));
			}
		});

	}
	public static final RecipeSerializer<SmithingSuitRecipe> SMITHING_SUIT_RECIPE_SERIALIZER = Registry.register(
			BuiltInRegistries.RECIPE_SERIALIZER,
			Identifier.fromNamespaceAndPath(MOD_ID, "smithing_suit"),
			new RecipeSerializer<>(SmithingSuitRecipe.MAP_CODEC, SmithingSuitRecipe.STREAM_CODEC)
	);
	public static final RecipeType<SmithingSuitRecipe> SMITHING_SUIT_RECIPE_TYPE = Registry.register(
			BuiltInRegistries.RECIPE_TYPE,
			Identifier.fromNamespaceAndPath(MOD_ID, "smithing_suit"),
			new RecipeType<SmithingSuitRecipe>() {}
	);
}