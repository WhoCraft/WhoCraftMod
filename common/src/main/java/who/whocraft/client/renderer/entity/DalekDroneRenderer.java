package who.whocraft.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import who.whocraft.Whocraft;
import who.whocraft.client.model.ModelRegistry;
import who.whocraft.client.model.entity.dalek.DalekDroneModel;
import who.whocraft.common.entity.WhocraftEntity;
import who.whocraft.common.entity.hostile.DalekDrone;

public class DalekDroneRenderer extends LivingEntityRenderer<DalekDrone, DalekDroneModel> {
    public DalekDroneRenderer(EntityRendererProvider.Context context) {

        super(context, new DalekDroneModel(context.bakeLayer(ModelRegistry.DALEK_DRONE)), 0f);
    }

    @Override
    public ResourceLocation getTextureLocation(DalekDrone entity) {
        return new ResourceLocation(Whocraft.MODID, "textures/entity/dalek/dalek_drone.png");
    }
}
