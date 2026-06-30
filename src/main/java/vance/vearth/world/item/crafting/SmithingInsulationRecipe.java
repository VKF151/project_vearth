package vance.vearth.world.item.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import org.jspecify.annotations.NonNull;
import vance.vearth.components.ModComponents;

import java.util.List;
import java.util.Optional;

public class SmithingInsulationRecipe extends SimpleSmithingRecipe {

    public static final MapCodec<SmithingInsulationRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(
            i -> i.group(
                            Recipe.CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
                            Ingredient.CODEC.fieldOf("template").forGetter(o -> o.template),
                            Ingredient.CODEC.fieldOf("base").forGetter(o -> o.base),
                            Ingredient.CODEC.fieldOf("addition").forGetter(o -> o.addition)
                    )
                    .apply(i, SmithingInsulationRecipe::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, SmithingInsulationRecipe> STREAM_CODEC = StreamCodec.composite(
            Recipe.CommonInfo.STREAM_CODEC,
            o -> o.commonInfo,
            Ingredient.CONTENTS_STREAM_CODEC,
            o -> o.template,
            Ingredient.CONTENTS_STREAM_CODEC,
            o -> o.base,
            Ingredient.CONTENTS_STREAM_CODEC,
            o -> o.addition,
            SmithingInsulationRecipe::new
    );
    public static final RecipeSerializer<SmithingInsulationRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
    private final Ingredient template;
    private final Ingredient base;
    private final Ingredient addition;

    public SmithingInsulationRecipe(
            final Recipe.CommonInfo commonInfo, final Ingredient template, final Ingredient base, final Ingredient addition
    ) {
        super(commonInfo);
        this.template = template;
        this.base = base;
        this.addition = addition;
    }

    public @NonNull ItemStack assemble(final SmithingRecipeInput input) {
        return applyInsulation(input.base(), input.addition());
    }

    public static ItemStack applyInsulation(final ItemStack baseItem, final ItemStack materialItem) {
        if (materialItem != null) {
            if (baseItem.has(ModComponents.INSULATED)) {
                return ItemStack.EMPTY;
            }

            ItemStack membranedItem = baseItem.copyWithCount(1);
            membranedItem.set(ModComponents.INSULATED, true);
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
    public @NonNull RecipeSerializer<SmithingInsulationRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    protected @NonNull PlacementInfo createPlacementInfo() {
        return PlacementInfo.create(List.of(this.template, this.base, this.addition));
    }
}
