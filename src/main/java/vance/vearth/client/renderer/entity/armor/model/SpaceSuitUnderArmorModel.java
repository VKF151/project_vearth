package vance.vearth.client.renderer.entity.armor.model;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;
import vance.vearth.Project_vearth;

import java.util.Set;

public class SpaceSuitUnderArmorModel<S extends HumanoidRenderState> extends HumanoidModel<S> {
    public static final ArmorModelSet<ModelLayerLocation> MODEL_LAYERS = new ArmorModelSet<>("helmet", "chestplate", "leggings", "boots").map( s -> new ModelLayerLocation(Identifier.fromNamespaceAndPath(Project_vearth.MOD_ID, "space_suit_under"), s));


    public SpaceSuitUnderArmorModel(ModelPart root) {
        super(root);
    }
    public static ArmorModelSet<LayerDefinition> createArmorMeshSet() {
        MeshDefinition head = createBaseArmorMesh();
        head.getRoot().retainPartsAndChildren(Set.of(PartNames.HEAD));
        MeshDefinition body = createBaseArmorMesh();
        body.getRoot().retainPartsAndChildren(Set.of(PartNames.BODY, PartNames.LEFT_ARM, PartNames.RIGHT_ARM));
        MeshDefinition legs = createBaseArmorMesh();
        legs.getRoot().retainPartsAndChildren(Set.of("left_leg_real", "right_leg_real"));
        MeshDefinition feet = createBaseArmorMesh();
        feet.getRoot().retainPartsAndChildren(Set.of(PartNames.LEFT_FOOT, PartNames.RIGHT_FOOT));

        ArmorModelSet<MeshDefinition> data = new ArmorModelSet<>(head, body, legs, feet);

        return data.map(d -> LayerDefinition.create(d, 64, 64));
    }

    private static MeshDefinition createBaseArmorMesh() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition head = root.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create().texOffs(0,0).addBox(-4, -8, -4, 8, 8, 8, new CubeDeformation(0.6F)), PartPose.ZERO);
        head.addOrReplaceChild(PartNames.HAT, CubeListBuilder.create(), PartPose.ZERO);

        PartDefinition body = root.addOrReplaceChild(PartNames.BODY, CubeListBuilder.create().texOffs(16, 16).addBox(-4, 0, -2, 8, 12, 4, new CubeDeformation(0.55F)), PartPose.ZERO);
        body.addOrReplaceChild("air_tank", CubeListBuilder.create().texOffs(16, 32).addBox(-4, -1, 2, 8, 10, 4, new CubeDeformation(0.5F)), PartPose.ZERO);

        root.addOrReplaceChild(PartNames.LEFT_ARM, CubeListBuilder.create().texOffs(32, 48).mirror().addBox(-1, -2, -2, 4, 12, 4, new CubeDeformation(0.25F)).mirror(false), PartPose.offset(5, 2, 0));
        root.addOrReplaceChild(PartNames.RIGHT_ARM, CubeListBuilder.create().texOffs(40, 16).addBox(-3, -2, -2, 4, 12, 4, new CubeDeformation(0.25F)), PartPose.offset(-5, 2, 0));

        PartDefinition leftLeg = root.addOrReplaceChild(PartNames.LEFT_LEG, CubeListBuilder.create(), PartPose.offset(1.9F, 12, 0));
        leftLeg.addOrReplaceChild("left_leg_real", CubeListBuilder.create().texOffs(16, 48).mirror().addBox(-2.1F, 0, -2, 4, 12, 4, new CubeDeformation(0.35F)).mirror(false), PartPose.ZERO);
        leftLeg.addOrReplaceChild(PartNames.LEFT_FOOT, CubeListBuilder.create().texOffs(0, 48).mirror().addBox(-2.1F, 0, -2, 4, 12, 4, new CubeDeformation(0.7F)).mirror(false), PartPose.ZERO);

        PartDefinition rightLeg = root.addOrReplaceChild(PartNames.RIGHT_LEG, CubeListBuilder.create(), PartPose.offset(-1.9F, 12, 0));
        rightLeg.addOrReplaceChild("right_leg_real", CubeListBuilder.create().texOffs(0, 16).addBox(-1.9F, 0, -2, 4, 12, 4, new CubeDeformation(0.35F)), PartPose.ZERO);
        rightLeg.addOrReplaceChild(PartNames.RIGHT_FOOT, CubeListBuilder.create().texOffs(0, 32).addBox(-1.9F, 0, -2, 4, 12, 4, new CubeDeformation(0.7F)), PartPose.ZERO);

        return mesh;
    }

}
