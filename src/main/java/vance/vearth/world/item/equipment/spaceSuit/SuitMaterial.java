package vance.vearth.world.item.equipment.spaceSuit;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.world.item.equipment.trim.MaterialAssetGroup;
import vance.vearth.resources.registry.ModRegistries;

public record SuitMaterial(MaterialAssetGroup assets, Component description) {

    public static final Codec<SuitMaterial> DIRECT_CODEC = RecordCodecBuilder.create((i) -> i.group(
            MaterialAssetGroup.MAP_CODEC.forGetter(SuitMaterial::assets),
            ComponentSerialization.CODEC.fieldOf("description").forGetter(SuitMaterial::description)
    ).apply(i, SuitMaterial::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SuitMaterial> DIRECT_STREAM_CODEC;
    public static final Codec<Holder<SuitMaterial>> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<SuitMaterial>> STREAM_CODEC;

    static {
        DIRECT_STREAM_CODEC = StreamCodec.composite(MaterialAssetGroup.STREAM_CODEC, SuitMaterial::assets, ComponentSerialization.STREAM_CODEC, SuitMaterial::description, SuitMaterial::new);
        CODEC = RegistryFileCodec.create(ModRegistries.SUIT_MATERIAL, DIRECT_CODEC);
        STREAM_CODEC = ByteBufCodecs.holder(ModRegistries.SUIT_MATERIAL, DIRECT_STREAM_CODEC);
    }
}
