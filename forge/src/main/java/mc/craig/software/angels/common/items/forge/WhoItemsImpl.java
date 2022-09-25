package mc.craig.software.angels.common.items.forge;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import who.whocraft.Whocraft;

public class WhoItemsImpl {

    public static CreativeModeTab TAB = new CreativeModeTab(Whocraft.MODID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Blocks.REDSTONE_WIRE);
        }
    };

    public static CreativeModeTab getCreativeTab() {
        return TAB;
    }


}