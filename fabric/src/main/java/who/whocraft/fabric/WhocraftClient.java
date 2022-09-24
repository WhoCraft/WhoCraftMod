package who.whocraft.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import who.whocraft.client.model.ModelRegistry;
import who.whocraft.client.renderer.entity.DalekDroneRenderer;
import who.whocraft.common.entity.WhocraftEntity;

public class WhocraftClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        entityRenders();
        ModelRegistry.init();
    }

    private void entityRenders() {
        EntityRendererRegistry.register(WhocraftEntity.DALEK.get(), DalekDroneRenderer::new);
    }
}
