package vance.vearth.world.item.equipment.spaceSuit;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import vance.vearth.resources.registry.ModRegistries;

public record SuitMaterial(Identifier paletteId, Component description) {

    public static final Codec<SuitMaterial> DIRECT_CODEC = RecordCodecBuilder.create((i) -> i.group(
            Identifier.CODEC.fieldOf("palette_id").forGetter(SuitMaterial::paletteId),
            ComponentSerialization.CODEC.fieldOf("description").forGetter(SuitMaterial::description)
    ).apply(i, SuitMaterial::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SuitMaterial> DIRECT_STREAM_CODEC;
    public static final Codec<Holder<SuitMaterial>> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<SuitMaterial>> STREAM_CODEC;

    static {
        DIRECT_STREAM_CODEC = StreamCodec.composite(Identifier.STREAM_CODEC, SuitMaterial::paletteId, ComponentSerialization.STREAM_CODEC, SuitMaterial::description, SuitMaterial::new);
        CODEC = RegistryCodecs.holder(ModRegistries.SUIT_MATERIAL, DIRECT_CODEC);
        STREAM_CODEC = ByteBufCodecs.holder(ModRegistries.SUIT_MATERIAL, DIRECT_STREAM_CODEC);
    }
}
