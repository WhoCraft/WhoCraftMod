package who.whocraft.common.block;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import who.whocraft.Whocraft;
import who.whocraft.common.item.WhoItems;
import who.whocraft.registry.DeferredRegistry;
import who.whocraft.registry.RegistrySupplier;

import java.util.function.Supplier;

public class WhoBlocks {

    public static final DeferredRegistry<Block> BLOCKS = DeferredRegistry.create(Whocraft.MODID, Registries.BLOCK);

    private static <T extends Block> RegistrySupplier<T> register(String id, Supplier<T> blockSupplier, CreativeModeTab itemGroup, boolean registerItem) {
        RegistrySupplier<T> registryObject = BLOCKS.register(id, blockSupplier);
        if(registerItem) {
            WhoItems.ITEMS.register(id, () -> new BlockItem(registryObject.get(), new Item.Properties()));
        }
        return registryObject;
    }

    private static <T extends Block> RegistrySupplier<T> register(String id, Supplier<T> blockSupplier, CreativeModeTab itemGroup) {
        return register(id, blockSupplier, itemGroup, true);
    }

}
