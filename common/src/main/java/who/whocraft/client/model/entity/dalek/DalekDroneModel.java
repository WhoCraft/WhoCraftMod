package who.whocraft.client.model.entity.dalek;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import who.whocraft.common.entity.hostile.DalekDrone;

public class DalekDroneModel extends HierarchicalModel<DalekDrone> {

	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart mid;
	private final ModelPart body;
	private final ModelPart eyestalk;
	private final ModelPart gunStick;

	public DalekDroneModel(ModelPart root) {
		this.root = root;
		this.head = this.root.getChild("head");
		this.mid = this.root.getChild("mid");
		this.body = this.root.getChild("body");
		this.eyestalk = this.head.getChild("eyestalk");
		this.gunStick = this.body.getChild("gunstick");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(54, 47).addBox(-3.5F, -1.0F, -3.5F, 7.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(40, 25).addBox(-1.5F, -1.0F, -4.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 47).addBox(-4.5F, 0.0F, -4.5F, 9.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 1.5F));

		PartDefinition eyestalk = head.addOrReplaceChild("eyestalk", CubeListBuilder.create().texOffs(41, 6).addBox(-1.0F, -2.0F, -6.175F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 7).addBox(-0.5F, -1.5F, -6.275F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 3).addBox(-1.0F, -2.0F, -3.775F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(8, 4).addBox(-1.0F, -2.0F, -3.275F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, -3.725F));

		PartDefinition lights = head.addOrReplaceChild("lights", CubeListBuilder.create(), PartPose.offset(4.5F, 1.0F, -1.0F));

		PartDefinition LLight_r1 = lights.addOrReplaceChild("LLight_r1", CubeListBuilder.create().texOffs(0, 7).addBox(-0.5F, -1.5F, 0.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.5F, 0.0F, 0.0F, 0.7854F));

		PartDefinition RLight_r1 = lights.addOrReplaceChild("RLight_r1", CubeListBuilder.create().texOffs(8, 0).addBox(-0.5F, -1.5F, 0.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(-9.0F, -1.0F, 0.5F, 0.0F, 0.0F, -0.7854F));

		PartDefinition mid = partdefinition.addOrReplaceChild("mid", CubeListBuilder.create().texOffs(0, 40).addBox(-4.5F, -7.5F, -3.5F, 9.0F, 5.0F, 9.0F, new CubeDeformation(-0.5F))
		.texOffs(0, 54).addBox(-4.5F, -7.0F, -3.5F, 9.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, 0.5F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(44, 5).addBox(-5.5F, -20.0F, -5.0F, 11.0F, 5.0F, 12.0F, new CubeDeformation(0.3F))
		.texOffs(0, 17).addBox(-5.5F, -20.0F, -5.0F, 11.0F, 11.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(33, 27).addBox(-5.5F, -9.0F, -6.0F, 11.0F, 7.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-6.5F, -2.0F, -7.0F, 13.0F, 2.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition plunger = body.addOrReplaceChild("plunger", CubeListBuilder.create().texOffs(0, 40).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(34, 17).addBox(-0.5F, -0.5F, -8.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, -17.0F, -5.0F));

		PartDefinition extend = plunger.addOrReplaceChild("extend", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, -5.625F, 1.0F, 1.0F, 6.0F, new CubeDeformation(-0.1F))
		.texOffs(0, 0).addBox(-1.0F, -1.0F, -6.525F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(41, 10).addBox(-1.5F, -1.5F, -7.525F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -3.5F));

		PartDefinition gunstick = body.addOrReplaceChild("gunstick", CubeListBuilder.create().texOffs(34, 25).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 17).addBox(-0.5F, -0.5F, -6.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(41, 0).addBox(-0.5F, -1.0F, -5.5F, 1.0F, 2.0F, 4.0F, new CubeDeformation(-0.1F))
		.texOffs(0, 23).addBox(-1.0F, -0.5F, -5.5F, 2.0F, 1.0F, 4.0F, new CubeDeformation(-0.1F)), PartPose.offset(3.5F, -17.0F, -5.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}


	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		mid.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}



	@Override
	public ModelPart root() {
		return this.root;
	}


	@Override
	public void setupAnim(DalekDrone entity, float f, float g, float h, float i, float j) {
		this.head.yRot = this.gunStick.yRot = i * 0.017453292F;
		this.eyestalk.xRot = this.gunStick.xRot = j * 0.017453292F;
	}




}