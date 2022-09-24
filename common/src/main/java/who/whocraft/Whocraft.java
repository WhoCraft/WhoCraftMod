package who.whocraft;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import who.whocraft.common.entity.WhocraftEntity;

public class Whocraft {

    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "whocraft";

    public static final String PLATFORM_ERROR = "Something has gone wrong with platform definitions. Please contact the mod author.";

    public static void init() {
        WhocraftEntity.ENTITY_TYPES.register();
    }
}