package vance.vearth.world.level.levelgen.feature;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.SequenceFeature;
import vance.vearth.Project_vearth;

public class ModFeatures {
    public static final ResourceKey<Feature> ENDOSELENE_PATCH = createKey("endoselene_patch");
    public static void bootstrap(final BootstrapContext<Feature> context) {
        context.register(ENDOSELENE_PATCH, new SequenceFeature(HolderSet.direct(new Holder[]{PlacementUtils.inlinePlaced(new EndoselenePatchFeature(10, 32, 64, 0, 1))})));
    }
    public static final MapCodec<EndoselenePatchFeature> ENDOSELENE_PATCH_TYPE = Registry.register(BuiltInRegistries.FEATURE_TYPE, Identifier.fromNamespaceAndPath(Project_vearth.MOD_ID, "endoselene_patch"), EndoselenePatchFeature.CODEC);

    public static ResourceKey<Feature> createKey(final String name) {
        return ResourceKey.create(Registries.FEATURE, Identifier.fromNamespaceAndPath(Project_vearth.MOD_ID, name));
    }

    public static void initialize() {}

}