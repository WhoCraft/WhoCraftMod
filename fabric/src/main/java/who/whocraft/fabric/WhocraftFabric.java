package who.whocraft.fabric;

import who.whocraft.Whocraft;
import net.fabricmc.api.ModInitializer;

public class WhocraftFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Whocraft.init();
    }
}