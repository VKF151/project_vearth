package vance.vearth.gravity;

import net.minecraft.util.Identifier;
import vance.vearth.Project_vearth;

import java.util.UUID;

public class GravityModifiers {

    private GravityModifiers() {}

    public static final Identifier MOON_ID = Identifier.of(Project_vearth.MOD_ID, "moon_gravity");
    public static final Identifier ORBIT_ID = Identifier.of(Project_vearth.MOD_ID, "orbit_gravity");


    public static final double MOON_GRAV = 0.013;
    public static final double EARTH_GRAV = 0.08;
    public static final double ORBIT_GRAV = 0.0;

}
