package vance.vearth.world.item.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import org.jspecify.annotations.NonNull;
import vance.vearth.Project_vearth;
import vance.vearth.components.ModComponents;

import java.util.List;
import java.util.Optional;

public class SmithingMembraneRecipe extends SimpleSmithingRecipe {
    private static final String LAYER_PATH = "textures/entity/equipment/humanoid_under/";
    private static final Identifier MEMBRANE = Identifier.fromNamespaceAndPath(Project_vearth.MOD_ID, LAYER_PATH + "membrane.png");

    public static final MapCodec<SmithingMembraneRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(
            i -> i.group(
                            Recipe.CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
                            Ingredient.CODEC.fieldOf("template").forGetter(o -> o.template),
                            Ingredient.CODEC.fieldOf("base").forGetter(o -> o.base),
                            Ingredient.CODEC.fieldOf("addition").forGetter(o -> o.addition)
                    )
                    .apply(i, SmithingMembraneRecipe::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, SmithingMembraneRecipe> STREAM_CODEC = StreamCodec.composite(
            Recipe.CommonInfo.STREAM_CODEC,
            o -> o.commonInfo,
            Ingredient.CONTENTS_STREAM_CODEC,
            o -> o.template,
            Ingredient.CONTENTS_STREAM_CODEC,
            o -> o.base,
            Ingredient.CONTENTS_STREAM_CODEC,
            o -> o.addition,
            SmithingMembraneRecipe::new
    );
    public static final RecipeSerializer<SmithingMembraneRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
    private final Ingredient template;
    private final Ingredient base;
    private final Ingredient addition;

    public SmithingMembraneRecipe(
            final Recipe.CommonInfo commonInfo, final Ingredient template, final Ingredient base, final Ingredient addition
    ) {
        super(commonInfo);
        this.template = template;
        this.base = base;
        this.addition = addition;
    }

    public @NonNull ItemStack assemble(final SmithingRecipeInput input) {
        return applyMembrane(input.base(), input.addition());
    }

    public static ItemStack applyMembrane(final ItemStack baseItem, final ItemStack materialItem) {
        if (materialItem != null) {
            if (baseItem.has(ModComponents.MEMBRANED)) {
                return ItemStack.EMPTY;
            }

            ItemStack membranedItem = baseItem.copyWithCount(1);
            membranedItem.set(ModComponents.MEMBRANED, true);
            membranedItem.set(ModComponents.ARMOR_LAYER, MEMBRANE);
            membranedItem.set(ModComponents.OXYGEN_STORAGE, 200);
            return membranedItem;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public @NonNull Optional<Ingredient> templateIngredient() {
        return Optional.of(this.template);
    }

    @Override
    public @NonNull Ingredient baseIngredient() {
        return this.base;
    }

    @Override
    public @NonNull Optional<Ingredient> additionIngredient() {
        return Optional.of(this.addition);
    }

    @Override
    public @NonNull RecipeSerializer<SmithingMembraneRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    protected @NonNull PlacementInfo createPlacementInfo() {
        return PlacementInfo.create(List.of(this.template, this.base, this.addition));
    }
}
