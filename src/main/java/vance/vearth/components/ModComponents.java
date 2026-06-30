package vance.vearth.components;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.core.Registry;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.resources.Identifier;
import vance.vearth.Project_vearth;

import java.util.function.UnaryOperator;

public class ModComponents {

    public static final DataComponentType<Integer> OXYGEN_STORAGE =
            register("oxygen_storage", integerBuilder -> integerBuilder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));

    public static final DataComponentType<Boolean> SEALED =
            register("sealed", booleanBuilder -> booleanBuilder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));

    public static final DataComponentType<Boolean> INSULATED =
            register("insulated", booleanBuilder -> booleanBuilder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));

    public static final DataComponentType<Boolean> MEMBRANED =
            register("membraned", booleanBuilder -> booleanBuilder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));

    public static final DataComponentType<Identifier> ARMOR_LAYER =
            register("armor_layer", identifierBuilder -> identifierBuilder.persistent(Identifier.CODEC).networkSynchronized(Identifier.STREAM_CODEC).cacheEncoding());

    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Identifier.fromNamespaceAndPath(Project_vearth.MOD_ID, name),
                builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void initialize() {
        Project_vearth.LOGGER.info("Registering components for " + Project_vearth.MOD_ID);
    }
}
