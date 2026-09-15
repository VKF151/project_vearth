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

public class SpaceSuitTankModel <S extends HumanoidRenderState> extends HumanoidModel<S> {
    public static final ArmorModelSet<ModelLayerLocation> MODEL_LAYERS = new ArmorModelSet<>("helmet", "chestplate", "leggings", "boots").map(s -> new ModelLayerLocation(Identifier.fromNamespaceAndPath(Project_vearth.MOD_ID, "space_suit_tank"), s));


    public SpaceSuitTankModel(ModelPart root) {
        super(root);
    }
    public static ArmorModelSet<LayerDefinition> createArmorMeshSet() {
        MeshDefinition head = createBaseArmorMesh();
        head.getRoot().retainPartsAndChildren(Set.of(PartNames.HEAD));
        MeshDefinition body = createBaseArmorMesh();
        body.getRoot().retainPartsAndChildren(Set.of(PartNames.BODY, PartNames.LEFT_ARM, PartNames.RIGHT_ARM));
        MeshDefinition legs = createBaseArmorMesh();
        MeshDefinition feet = createBaseArmorMesh();
        feet.getRoot().retainPartsAndChildren(Set.of(PartNames.LEFT_FOOT, PartNames.RIGHT_FOOT));

        ArmorModelSet<MeshDefinition> data = new ArmorModelSet<>(head, body, legs, feet);

        return data.map(d -> LayerDefinition.create(d, 64, 32));
    }

    private static MeshDefinition createBaseArmorMesh() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition head = root.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create(), PartPose.ZERO);
        head.addOrReplaceChild(PartNames.HAT, CubeListBuilder.create(), PartPose.ZERO);

        PartDefinition body = root.addOrReplaceChild(PartNames.BODY, CubeListBuilder.create(), PartPose.ZERO);
        body.addOrReplaceChild("air_tank", CubeListBuilder.create().texOffs(0, 1).addBox(-4, -1, -5, 8, 10, 2, new CubeDeformation(0.6F)), PartPose.rotation(0.0F, (float) Math.PI, 0.0F));

        root.addOrReplaceChild(PartNames.LEFT_ARM, CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild(PartNames.RIGHT_ARM, CubeListBuilder.create(), PartPose.ZERO);

        PartDefinition leftLeg = root.addOrReplaceChild(PartNames.LEFT_LEG, CubeListBuilder.create(), PartPose.ZERO);
        leftLeg.addOrReplaceChild(PartNames.LEFT_FOOT, CubeListBuilder.create(), PartPose.ZERO);

        PartDefinition rightLeg = root.addOrReplaceChild(PartNames.RIGHT_LEG, CubeListBuilder.create(), PartPose.ZERO);
        rightLeg.addOrReplaceChild(PartNames.RIGHT_FOOT, CubeListBuilder.create(), PartPose.ZERO);

        return mesh;
    }

}
