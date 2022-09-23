package who.whocraft.forge;

import dev.architectury.platform.forge.EventBuses;
import who.whocraft.Whocraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Whocraft.MODID)
public class WhocraftForge {
    public WhocraftForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(Whocraft.MODID, FMLJavaModLoadingContext.get().getModEventBus());
        Whocraft.init();
    }
}