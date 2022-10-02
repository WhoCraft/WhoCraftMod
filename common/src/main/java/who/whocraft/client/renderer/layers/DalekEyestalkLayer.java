package who.whocraft.client.renderer.layers;

import net.minecraft.client.model.EndermanModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import who.whocraft.Whocraft;
import who.whocraft.client.model.entity.dalek.DalekDroneModel;
import who.whocraft.common.entity.hostile.DalekDrone;

public class DalekEyestalkLayer extends EyesLayer<DalekDrone, DalekDroneModel> {
    private static final RenderType DALEK_EYE_GLOW = RenderType.eyes(new ResourceLocation(Whocraft.MODID, "textures/entity/dalek/dalek_eyestalk_glow.png"));
    public DalekEyestalkLayer(RenderLayerParent<DalekDrone, DalekDroneModel> renderLayerParent) {
        super(renderLayerParent);
    }

    @Override
    public RenderType renderType() {
        return DALEK_EYE_GLOW;
    }
}
