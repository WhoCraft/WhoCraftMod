package who.whocraft.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;
import who.whocraft.client.model.entity.dalek.GeoDalekModel;
import who.whocraft.common.entity.hostile.DalekDrone;

public class DalekRendererGecko extends GeoEntityRenderer<DalekDrone> {

    public DalekRendererGecko(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GeoDalekModel());
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }


}
