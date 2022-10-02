package who.whocraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EndermanRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import who.whocraft.Whocraft;
import who.whocraft.client.model.ModelRegistry;
import who.whocraft.client.model.entity.dalek.DalekDroneModel;
import who.whocraft.client.renderer.layers.DalekEyestalkLayer;
import who.whocraft.common.entity.WhocraftEntity;
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
    public void render(DalekDrone livingEntity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        super.render(livingEntity, f, g, poseStack, multiBufferSource, i);
        if (livingEntity.getIsFiring()) {
            poseStack.pushPose();
            Vec3 vec1 = new Vec3(livingEntity.xOld, livingEntity.yOld,livingEntity.zOld);
            Vec3 vec2 = new Vec3(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());

            double x_ = vec2.x - vec1.x;
            double y_ = vec2.y - vec1.y;
            double z_ = vec2.z - vec1.z;
            double diff = Mth.sqrt((float) (x_ * x_ + z_ * z_));
            float yaw = -livingEntity.getYHeadRot();
            //float yaw = (float) (Math.atan2(z_, x_) * 180.0D / 3.141592653589793D) - 90.0F;
            poseStack.mulPose(Vector3f.YP.rotationDegrees(yaw));
            poseStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
            poseStack.translate(0.2175f, 0.25f ,-1.06f);
            VertexConsumer vertexBuilder = multiBufferSource.getBuffer(RenderType.lightning());
            RenderHelper.drawGlowingLine(poseStack, vertexBuilder, 100f, 0.1F, (float) livingEntity.LASER_COLOR.getRed(), (float) livingEntity.LASER_COLOR.getGreen(), (float) livingEntity.LASER_COLOR.getBlue(), 1F, 15728640);
            poseStack.popPose();
        }






    }
}
