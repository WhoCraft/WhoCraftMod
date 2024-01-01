package who.whocraft.client.model.entity.dalek;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import who.whocraft.Whocraft;
import who.whocraft.common.entity.hostile.DalekDrone;

public class GeoDalekModel extends GeoModel<DalekDrone> {

    private final ResourceLocation modelResource = new ResourceLocation(Whocraft.MODID, "geo/dalek.geo.json");
    private final ResourceLocation textureResource = new ResourceLocation(Whocraft.MODID, "textures/entity/dalek.png");
    private final ResourceLocation animationResource = new ResourceLocation(Whocraft.MODID, "animations/dalek.idle.json");


    @Override
    public ResourceLocation getModelResource(DalekDrone animatable) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(DalekDrone animatable) {
        return textureResource;
    }

    @Override
    public ResourceLocation getAnimationResource(DalekDrone animatable) {
        return animationResource;
    }
}
