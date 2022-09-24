package who.whocraft.forge.handlers;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import who.whocraft.Whocraft;
import who.whocraft.client.model.ModelRegistry;
import who.whocraft.client.model.forge.ModelRegistryImpl;
import who.whocraft.client.renderer.entity.DalekDroneRenderer;
import who.whocraft.common.entity.WhocraftEntity;

@Mod.EventBusSubscriber(modid = Whocraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModBus {

    @SubscribeEvent
    public static void event(EntityRenderersEvent.RegisterLayerDefinitions event) {
        ModelRegistry.init();
        ModelRegistryImpl.register(event);
    }

    @SubscribeEvent
    public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(WhocraftEntity.DALEK.get(), DalekDroneRenderer::new);
    }
}
