package vance.vearth.world.item.equipment.spaceSuit;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public record RespirantStorage(int respirantAmount, int maxRespirantAmount) implements TooltipProvider {
    public static final Codec<RespirantStorage> CODEC = RecordCodecBuilder.create((i) -> i.group(
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("respirant_amount").forGetter(RespirantStorage::respirantAmount),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("max_respirant_amount").forGetter(RespirantStorage::maxRespirantAmount)
    ).apply(i, RespirantStorage::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, RespirantStorage> STREAM_CODEC;

    @Override
    public void addToTooltip(Item.@NonNull TooltipContext context, Consumer<Component> consumer, @NonNull TooltipFlag flag, @NonNull DataComponentGetter components) {
        consumer.accept(CommonComponents.space().append(this.respirantAmount + "/" + this.maxRespirantAmount));
    }

    static {
        STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, RespirantStorage::respirantAmount, ByteBufCodecs.VAR_INT, RespirantStorage::maxRespirantAmount, RespirantStorage::new);
    }

}
