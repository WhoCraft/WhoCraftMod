package who.whocraft;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import org.slf4j.Logger;
import who.whocraft.common.block.WhoBlocks;
import who.whocraft.common.blockentity.WhoBlockEntities;
import who.whocraft.common.WhocraftSound;
import who.whocraft.common.entity.WhocraftEntity;
import who.whocraft.common.item.WhoItems;

public class Whocraft {

    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "whocraft";

    public static final String PLATFORM_ERROR = "Something has gone wrong with platform definitions. Please contact the mod author.";

    public static void init() {
        WhocraftEntity.ENTITY_TYPES.register();
        WhoItems.ITEMS.register();
        WhoBlocks.BLOCKS.register();
        WhoBlockEntities.BLOCK_ENTITY_TYPES.register();

        WhocraftSound.SOUNDS.register();
    }
}