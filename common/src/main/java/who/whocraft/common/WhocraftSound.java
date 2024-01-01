package who.whocraft.common;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import who.whocraft.Whocraft;
import who.whocraft.registry.DeferredRegistry;
import who.whocraft.registry.RegistrySupplier;

public class WhocraftSound {

    public static final DeferredRegistry<SoundEvent> SOUNDS = DeferredRegistry.create(Whocraft.MODID, Registries.SOUND_EVENT);

    public static final RegistrySupplier<SoundEvent> DALEK_GUN_CHARGE = setUpSound("dalek_gun_charge");
    public static final RegistrySupplier<SoundEvent> DALEK_GUN_FIRE = setUpSound("dalek_gun_fire");
    public static final RegistrySupplier<SoundEvent> DALEK_EXTERMINATE = setUpSound("dalek_exterminate");
    public static final RegistrySupplier<SoundEvent> DALEK_ALERT = setUpSound("dalek_alert");

    private static RegistrySupplier<SoundEvent> setUpSound(String soundName) {
        SoundEvent sound =  SoundEvent.createFixedRangeEvent(new ResourceLocation(Whocraft.MODID, soundName), 1);
        return SOUNDS.register(soundName, () -> sound);
    }
}
