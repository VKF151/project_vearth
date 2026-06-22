package vance.vearth.components;

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

    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Identifier.fromNamespaceAndPath(Project_vearth.MOD_ID, name),
                builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void initialize() {
        Project_vearth.LOGGER.info("Registering components for " + Project_vearth.MOD_ID);
    }
}
