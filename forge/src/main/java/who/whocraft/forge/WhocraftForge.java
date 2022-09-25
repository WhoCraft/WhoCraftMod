package who.whocraft.forge;

import who.whocraft.Whocraft;
import net.minecraftforge.fml.common.Mod;

@Mod(Whocraft.MODID)
public class WhocraftForge {
    public WhocraftForge() {
        Whocraft.init();
    }
}