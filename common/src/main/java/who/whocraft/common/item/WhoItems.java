package who.whocraft.common.item;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import who.whocraft.Whocraft;
import who.whocraft.registry.DeferredRegistry;
import who.whocraft.registry.RegistrySupplier;

public class WhoItems {


    public static CreativeModeTab MAIN_TAB = null;

    static {
        if (MAIN_TAB == null) {
            MAIN_TAB = getCreativeTab();
        }
    }

    public static final DeferredRegistry<Item> ITEMS = DeferredRegistry.create(Whocraft.MODID, Registry.ITEM_REGISTRY);


    @ExpectPlatform
    public static CreativeModeTab getCreativeTab() {
        throw new RuntimeException(Whocraft.PLATFORM_ERROR);
    }

}
