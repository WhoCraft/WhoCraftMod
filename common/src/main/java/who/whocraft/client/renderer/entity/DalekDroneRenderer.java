package who.whocraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import who.whocraft.Whocraft;
import who.whocraft.client.model.ModelRegistry;
import who.whocraft.client.model.entity.dalek.DalekDroneModel;
import who.whocraft.client.renderer.layers.DalekEyestalkLayer;
import who.whocraft.common.entity.hostile.DalekDrone;
import who.whocraft.common.util.RenderHelper;

public class DalekDroneRenderer extends LivingEntityRenderer<DalekDrone, DalekDroneModel> {

    public DalekDroneRenderer(EntityRendererProvider.Context context) {
        super(context, new DalekDroneModel(context.bakeLayer(ModelRegistry.DALEK_DRONE)), 0f);
        addLayer(new DalekEyestalkLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(DalekDrone entity) {
        return new ResourceLocation(Whocraft.MODID, "textures/entity/dalek/dalek_drone.png");
    }

    @Override
    protected void setupRotations(DalekDrone livingEntity, PoseStack poseStack, float f, float g, float h) {
        super.setupRotations(livingEntity, poseStack, f, g, h);
    }

    @Override
    public void render(DalekDrone dalek, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        super.render(dalek, f, g, poseStack, multiBufferSource, i);

        var livingEntity = dalek.getActiveAttackTarget();
        if (livingEntity != null && dalek.getIsFiring()) {

            float l = dalek.getEyeHeight() - 0.5f;

            poseStack.pushPose();
            poseStack.translate(0.0D, (double)l, 0.0D);
            Vec3 vec3 = this.getPosition(livingEntity, (double)livingEntity.getBbHeight() * 0.5D, g);
            Vec3 vec32 = this.getPosition(dalek, (double)l, g);
            Vec3 vec33 = vec3.subtract(vec32);
            vec33 = vec33.normalize();
            float n = (float)Math.acos(vec33.y);
            float o = (float)Math.atan2(vec33.z, vec33.x);
            poseStack.mulPose(Axis.YP.rotationDegrees((1.5707964F - o) * 57.295776F));
            poseStack.mulPose(Axis.XP.rotationDegrees(n * 57.295776F));

            float distance = (float)dalek.position().distanceToSqr(livingEntity.position()) / 100;

            VertexConsumer vertexBuilder = multiBufferSource.getBuffer(RenderType.lightning());
            RenderHelper.drawGlowingLine(poseStack, vertexBuilder, distance, 0.1F, 255, 255, 255, 1F, 15728640);
            poseStack.popPose();
        }

    }

    private Vec3 getPosition(LivingEntity livingEntity, double d, float f) {
        double e = Mth.lerp((double)f, livingEntity.xOld, livingEntity.getX());
        double g = Mth.lerp((double)f, livingEntity.yOld, livingEntity.getY()) + d;
        double h = Mth.lerp((double)f, livingEntity.zOld, livingEntity.getZ());
        return new Vec3(e, g, h);
    }


    //private static final ResourceLocation GUARDIAN_LOCATION = new ResourceLocation("textures/entity/guardian.png");
    private static final ResourceLocation GUARDIAN_BEAM_LOCATION = new ResourceLocation(Whocraft.MODID, "textures/entity/dalek/dalek_beam.png");
    private static final RenderType BEAM_RENDER_TYPE;

    static {
        BEAM_RENDER_TYPE = RenderType.entityCutoutNoCull(GUARDIAN_BEAM_LOCATION);
    }
}
