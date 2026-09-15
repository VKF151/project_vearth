package vance.vearth.world.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import org.jspecify.annotations.NonNull;
import vance.vearth.components.ModComponents;
import vance.vearth.world.item.equipment.spaceSuit.RespirantStorage;
import vance.vearth.world.item.equipment.spaceSuit.SpaceSuit;
import vance.vearth.world.item.equipment.spaceSuit.SuitDesign;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SmithingSuitRecipe extends SimpleSmithingRecipe {
    private final Ingredient template;
    private final Ingredient base;
    private final Ingredient addition;
    private final Holder<SuitDesign> design;
    private final boolean hasRespirantStorage;
    private final Integer maxRespirantStorage;

    protected SmithingSuitRecipe(Recipe.CommonInfo commonInfo, final Ingredient template, final Ingredient base, final Ingredient addition, Holder<SuitDesign> design, boolean hasRespirantStorage, Integer maxRespirantStorage) {
        super(commonInfo);
        this.template = template;
        this.base = base;
        this.addition = addition;
        this.design = design;
        this.hasRespirantStorage = hasRespirantStorage;
        this.maxRespirantStorage = maxRespirantStorage;
    }

    public static final MapCodec<SmithingSuitRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
            Ingredient.CODEC.fieldOf("template").forGetter(o -> o.template),
            Ingredient.CODEC.fieldOf("base").forGetter(o -> o.base),
            Ingredient.CODEC.fieldOf("addition").forGetter(o -> o.addition),
            SuitDesign.CODEC.fieldOf("design").forGetter(o -> o.design),
            Codec.BOOL.fieldOf("has_respirant_storage").forGetter(o -> o.hasRespirantStorage),
            Codec.INT.optionalFieldOf("max_respirant_storage", 24000).forGetter(o -> o.maxRespirantStorage)
    ).apply(i, SmithingSuitRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SmithingSuitRecipe> STREAM_CODEC = StreamCodec.composite(
            Recipe.CommonInfo.STREAM_CODEC,
            o -> o.commonInfo,
            Ingredient.CONTENTS_STREAM_CODEC,
            o -> o.template,
            Ingredient.CONTENTS_STREAM_CODEC,
            o -> o.base,
            Ingredient.CONTENTS_STREAM_CODEC,
            o -> o.addition,
            SuitDesign.STREAM_CODEC,
            o -> o.design,
            ByteBufCodecs.BOOL,
            o -> o.hasRespirantStorage,
            ByteBufCodecs.VAR_INT,
            o -> o.maxRespirantStorage,
            SmithingSuitRecipe::new
    );
    public static final RecipeSerializer<SmithingSuitRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    @Override
    public @NonNull ItemStack assemble(SmithingRecipeInput input) {
        return applySuit(input.base(), input.addition(), this.design, this.hasRespirantStorage, this.maxRespirantStorage);
    }

    public static ItemStack applySuit(final ItemStack baseItem, final ItemStack materialItem, final Holder<SuitDesign> pattern, boolean hasRespirantStorage, int maxRespirantStorage) {
        Holder<TrimMaterial> material = materialItem.get(DataComponents.PROVIDES_TRIM_MATERIAL);
        if (material != null) {
            SpaceSuit existingSuit = baseItem.get(ModComponents.SUIT);
            SpaceSuit newSuit = new SpaceSuit(material, pattern);
            if (Objects.equals(existingSuit, newSuit)) {
                return ItemStack.EMPTY;
            }

            ItemStack SuitItem = baseItem.copyWithCount(1);
            if (hasRespirantStorage) {
                SuitItem.set(ModComponents.RESPIRANT_STORAGE, new RespirantStorage(0, maxRespirantStorage));
            }
            SuitItem.set(ModComponents.SUIT, newSuit);
            return SuitItem;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public @NonNull RecipeSerializer<? extends SimpleSmithingRecipe> getSerializer() {
        return SERIALIZER;
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
    protected @NonNull PlacementInfo createPlacementInfo() {
        return PlacementInfo.create(List.of(this.template, this.base, this.addition));
    }
}
