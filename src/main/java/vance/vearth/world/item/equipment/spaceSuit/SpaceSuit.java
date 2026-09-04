package vance.vearth.world.item.equipment.spaceSuit;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.trim.MaterialAssetGroup;
import org.jspecify.annotations.NonNull;
import vance.vearth.Project_vearth;

import java.util.function.Consumer;

public record SpaceSuit(Holder<SuitMaterial> material, Holder<SuitDesign> design) implements TooltipProvider {
    public static final Codec<SpaceSuit> CODEC = RecordCodecBuilder.create((i) -> i.group(SuitMaterial.CODEC.fieldOf("material").forGetter(SpaceSuit::material), SuitDesign.CODEC.fieldOf("design").forGetter(SpaceSuit::design)).apply(i, SpaceSuit::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, SpaceSuit> STREAM_CODEC;
    public static final Component UPGRADE_TITLE;

    @Override
    public void addToTooltip(Item.@NonNull TooltipContext context, Consumer<Component> consumer, @NonNull TooltipFlag flag, @NonNull DataComponentGetter components) {
        consumer.accept(UPGRADE_TITLE);
        consumer.accept(CommonComponents.space().append((this.design.value()).copyWithStyle(this.material)));
        consumer.accept(CommonComponents.space().append((this.material.value()).description()));
    }

    public Identifier layerAssetId(final String layerAssetPrefix, final ResourceKey<EquipmentAsset> equipmentAsset) {
        MaterialAssetGroup.AssetInfo materialAsset = (this.material().value()).assets().assetId(equipmentAsset);
        return (this.design().value()).assetId().withPath((designPath) -> layerAssetPrefix + "/" + designPath + "_" + materialAsset.suffix());
    }

    static {
        STREAM_CODEC = StreamCodec.composite(SuitMaterial.STREAM_CODEC, SpaceSuit::material, SuitDesign.STREAM_CODEC, SpaceSuit::design, SpaceSuit::new);
        UPGRADE_TITLE = Component.translatable(Util.makeDescriptionId("item", Identifier.fromNamespaceAndPath(Project_vearth.MOD_ID, "space_suit.upgrade"))).withStyle(ChatFormatting.GRAY);
    }
}
