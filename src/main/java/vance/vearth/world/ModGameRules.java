package vance.vearth.world;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import vance.vearth.Project_vearth;

public class ModGameRules {
    public static final GameRule<Integer> DEFAULT_MAX_OXYGEN_STORAGE_GAMERULE = GameRuleBuilder.forInteger(24000).category(GameRuleCategory.MISC).buildAndRegister(Identifier.fromNamespaceAndPath(Project_vearth.MOD_ID, "default_max_oxygen_storage"));

    public static void initialize() {}
}
