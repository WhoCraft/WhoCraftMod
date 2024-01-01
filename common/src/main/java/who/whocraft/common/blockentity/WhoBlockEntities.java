package who.whocraft.common.blockentity;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import who.whocraft.Whocraft;
import who.whocraft.registry.DeferredRegistry;

public class WhoBlockEntities {

    public static final DeferredRegistry<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegistry.create(Whocraft.MODID, Registries.BLOCK_ENTITY_TYPE);

}
