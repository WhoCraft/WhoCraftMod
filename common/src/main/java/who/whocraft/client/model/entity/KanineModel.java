package who.whocraft.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import who.whocraft.common.entity.nuetral.Kanine;

public class KanineModel extends HierarchicalModel<Kanine> {


    public static final AnimationDefinition EAR_CYCLE = AnimationDefinition.Builder.withLength(1f).looping().addAnimation("leftEar", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 52.5f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.LINEAR))).addAnimation("rightEar", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, -52.5f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.LINEAR))).build();
    public static final AnimationDefinition POWERED_DOWN = AnimationDefinition.Builder.withLength(0f).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe(0f, KeyframeAnimations.posVec(0f, -1f, -1f), AnimationChannel.Interpolations.LINEAR))).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0f, KeyframeAnimations.degreeVec(30f, 0f, 0f), AnimationChannel.Interpolations.LINEAR))).addAnimation("tail", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0f, KeyframeAnimations.degreeVec(-35f, 0f, 0f), AnimationChannel.Interpolations.LINEAR))).build();
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart neck;
    private final ModelPart tail;
    private final ModelPart root;

    public KanineModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.head = root.getChild("head");
        this.neck = root.getChild("neck");
        this.tail = root.getChild("tail");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 36).addBox(-4.0F, -7.0F, -7.0F, 8.0F, 5.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(36, 18).addBox(-4.0F, -10.0F, -5.0F, 2.0F, 5.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(36, 0).addBox(-3.0F, -11.0F, -6.0F, 6.0F, 4.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(0, 14).addBox(-1.0F, -12.0F, -5.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 8).addBox(-1.0F, -7.0F, -8.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 18).addBox(-5.0F, -2.0F, -8.0F, 10.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-5.0F, -2.0F, -8.0F, 10.0F, 2.0F, 16.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(30, 36).addBox(-2.0F, -3.0F, -3.0F, 4.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 18).addBox(-1.5F, -1.0F, -6.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(6, 11).addBox(-1.0F, -1.0F, -7.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 4).addBox(-0.5F, -1.0863F, -7.0972F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.625F, -8.575F, 0.1309F, 0.0F, 0.0F));

        PartDefinition gun = head.addOrReplaceChild("gun", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, 12.5F, -1.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, -13.525F, -5.675F));

        PartDefinition ears = head.addOrReplaceChild("ears", CubeListBuilder.create(), PartPose.offset(0.0F, 2.0F, 1.0F));

        PartDefinition l = ears.addOrReplaceChild("leftEar", CubeListBuilder.create().texOffs(4, 7).addBox(-1.0F, -0.25F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 1).addBox(-0.5F, 0.75F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -6.75F, -2.0F, 0.0F, -0.48F, 0.0F));

        PartDefinition r = ears.addOrReplaceChild("rightEar", CubeListBuilder.create().texOffs(6, 7).addBox(-1.0F, -0.25F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 1).addBox(-0.5F, 0.75F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -6.75F, -2.0F, 0.0F, 0.48F, 0.0F));

        PartDefinition bone = head.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition neck = partdefinition.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(42, 40).addBox(-1.5F, -1.0925F, -5.8349F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 16.0F, -5.0F, -0.8727F, 0.0F, 0.0F));

        PartDefinition Tag_r1 = neck.addOrReplaceChild("Tag_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.125F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.9026F, -1.8286F, 0.8727F, 0.0F, 0.0F));

        PartDefinition tail = partdefinition.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(5, 0).addBox(-0.5F, -0.5F, 10.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 2).addBox(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 11).addBox(-0.5F, -0.5F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.0F, 5.0F, 0.1745F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        neck.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        tail.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return root;
    }

    @Override
    public void setupAnim(Kanine entity, float f, float g, float h, float i, float j) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animate(entity.EARS, EAR_CYCLE, entity.tickCount);
        this.animate(entity.POWERED_DOWN, POWERED_DOWN, entity.tickCount);
    }
}