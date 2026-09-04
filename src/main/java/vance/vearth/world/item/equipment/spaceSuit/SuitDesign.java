package vance.vearth.world.item.equipment.spaceSuit;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.util.ExtraCodecs;
import vance.vearth.resources.registry.ModRegistries;

public record SuitDesign(Identifier assetId, Component description, boolean decal) {
    public static final Codec<SuitDesign> DIRECT_CODEC = RecordCodecBuilder.create((i) -> i.group(Identifier.CODEC.fieldOf("asset_id").forGetter(SuitDesign::assetId), ComponentSerialization.CODEC.fieldOf("description").forGetter(SuitDesign::description), ExtraCodecs.optionalAlwaysPresentFieldOf(Codec.BOOL, "decal", false).forGetter(SuitDesign::decal)).apply(i, SuitDesign::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, SuitDesign> DIRECT_STREAM_CODEC;
    public static final Codec<Holder<SuitDesign>> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<SuitDesign>> STREAM_CODEC;

    public Component copyWithStyle(final Holder<SuitMaterial> material) {
        return this.description.copy().withStyle((material.value()).description().getStyle());
    }

    static {
        DIRECT_STREAM_CODEC = StreamCodec.composite(Identifier.STREAM_CODEC, SuitDesign::assetId, ComponentSerialization.STREAM_CODEC, SuitDesign::description, ByteBufCodecs.BOOL, SuitDesign::decal, SuitDesign::new);
        CODEC = RegistryFileCodec.create(ModRegistries.SUIT_DESIGN, DIRECT_CODEC);
        STREAM_CODEC = ByteBufCodecs.holder(ModRegistries.SUIT_DESIGN, DIRECT_STREAM_CODEC);
    }
}
