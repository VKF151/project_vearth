package vance.vearth.world.dimension;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import vance.vearth.Project_vearth;

public class ModDims {
        public static final RegistryKey<World> MOON_KEY = RegistryKey.of(RegistryKeys.WORLD,
                Identifier.of(Project_vearth.MOD_ID, "moon"));
        public static final RegistryKey<World> ORBIT_KEY = RegistryKey.of(RegistryKeys.WORLD,
            Identifier.of(Project_vearth.MOD_ID, "orbit"));


}
