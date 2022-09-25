package who.whocraft.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import who.whocraft.Whocraft;
import who.whocraft.client.model.ModelRegistry;
import who.whocraft.client.model.entity.KanineModel;
import who.whocraft.common.entity.nuetral.Kanine;

public class KanineRenderer extends LivingEntityRenderer<Kanine, KanineModel> {

    public static ResourceLocation TEXTURE = new ResourceLocation(Whocraft.MODID, "textures/entity/tameable/kanine.png");
    public static ResourceLocation DISABLED = new ResourceLocation(Whocraft.MODID, "textures/entity/tameable/kanine_damaged.png");

    public KanineRenderer(EntityRendererProvider.Context context) {
        super(context, new KanineModel(context.bakeLayer(ModelRegistry.KANINE)), 0.3f);
    }

    @Override
    public ResourceLocation getTextureLocation(Kanine kanine) {
        return kanine.isDisabled() ? DISABLED : TEXTURE;
    }

    @Override
    protected boolean shouldShowName(Kanine livingEntity) {
        return false;
    }
}
