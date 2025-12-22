package vance.vearth.components;

import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import vance.vearth.Project_vearth;
import net.minecraft.util.dynamic.Codecs;

import java.util.function.UnaryOperator;

public class ModComponents {

    public static final ComponentType<Integer> OXYGEN_STORAGE =
            register("oxygen_storage", integerBuilder -> integerBuilder.codec(Codecs.NON_NEGATIVE_INT).packetCodec(PacketCodecs.VAR_INT));

    private static <T> ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(Project_vearth.MOD_ID, name),
                builderOperator.apply(ComponentType.builder()).build());
    }

    public static void initialize() {
        Project_vearth.LOGGER.info("Registering components for " + Project_vearth.MOD_ID);
    }
}
