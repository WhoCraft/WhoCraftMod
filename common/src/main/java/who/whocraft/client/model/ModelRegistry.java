package who.whocraft.client.model;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;
import who.whocraft.Whocraft;
import who.whocraft.client.model.entity.KanineModel;
import who.whocraft.client.model.entity.dalek.DalekDroneModel;
import who.whocraft.common.entity.hostile.DalekDrone;

import java.util.function.Supplier;

public class ModelRegistry {

    public static ModelLayerLocation DALEK_DRONE,KANINE;

    public static void init() {
        DALEK_DRONE = register(new ModelLayerLocation(new ResourceLocation(Whocraft.MODID, "dalek_drone"), "dalek_drone"), DalekDroneModel::createBodyLayer);
        KANINE = register(new ModelLayerLocation(new ResourceLocation(Whocraft.MODID, "k9"), "k9"), KanineModel::createBodyLayer);
    }


    @ExpectPlatform
    public static ModelLayerLocation register(ModelLayerLocation location, Supplier<LayerDefinition> definition) {
        throw new RuntimeException(Whocraft.PLATFORM_ERROR);
    }


}
