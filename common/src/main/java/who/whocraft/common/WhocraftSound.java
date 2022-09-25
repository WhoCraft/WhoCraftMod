package who.whocraft.common;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import who.whocraft.Whocraft;
import who.whocraft.registry.DeferredRegistry;
import who.whocraft.registry.RegistrySupplier;

public class WhocraftSound {

    public static final DeferredRegistry<SoundEvent> SOUNDS = DeferredRegistry.create(Whocraft.MODID, Registry.SOUND_EVENT_REGISTRY);

    public static final RegistrySupplier<SoundEvent> DALEK_GUN_CHARGE = setUpSound("dalek_gun_charge");

    private static RegistrySupplier<SoundEvent> setUpSound(String soundName) {
        SoundEvent sound = new SoundEvent(new ResourceLocation(Whocraft.MODID, soundName));
        return SOUNDS.register(soundName, () -> sound);
    }
}
