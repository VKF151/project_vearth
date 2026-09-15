package vance.vearth.world.effects;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import vance.vearth.Project_vearth;
import vance.vearth.resources.Identifier.ModPotionIds;

public class ModMobEffects {
    public static final Holder<MobEffect> BREATH = register("breath", new BreathMobEffect(MobEffectCategory.BENEFICIAL, 99999900));
    public static final Holder<Potion> BREATH_POTION =
            Registry.registerForHolder(
                    BuiltInRegistries.POTION,
                    ModPotionIds.BREATH,
                    new Potion("breath", new MobEffectInstance(BREATH, 1))
                    );

    private static Holder<MobEffect> register(final String name, final MobEffect mobEffect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Identifier.fromNamespaceAndPath(Project_vearth.MOD_ID, name), mobEffect);
    }

    public static void initialize() {}
}
