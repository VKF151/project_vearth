package vance.vearth.world.dimension;

import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import vance.vearth.Project_vearth;

public class ModDims {
        public static final ResourceKey<Level> MOON_KEY = ResourceKey.create(Registries.DIMENSION,
                Identifier.fromNamespaceAndPath(Project_vearth.MOD_ID, "moon"));
        public static final ResourceKey<Level> ORBIT_KEY = ResourceKey.create(Registries.DIMENSION,
            Identifier.fromNamespaceAndPath(Project_vearth.MOD_ID, "orbit"));


}
