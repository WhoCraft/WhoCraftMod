package who.whocraft.common.entity;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import who.whocraft.common.entity.hostile.AbstractDalek;
import who.whocraft.common.entity.hostile.DalekDrone;
import who.whocraft.common.entity.nuetral.Kanine;
import who.whocraft.registry.DeferredRegistry;
import who.whocraft.registry.RegistrySupplier;

import java.util.function.Supplier;

import static who.whocraft.Whocraft.MODID;

public class WhocraftEntity {

    public static final DeferredRegistry<EntityType<?>> ENTITY_TYPES = DeferredRegistry.create(MODID, Registry.ENTITY_TYPE_REGISTRY);

    public static final RegistrySupplier<EntityType<DalekDrone>> DALEK = ENTITY_TYPES.register("dalek", () -> EntityType.Builder.of((EntityType.EntityFactory<DalekDrone>) (entityType, level) -> new DalekDrone(entityType, level), MobCategory.CREATURE).sized(0.6F, 1.95F).build(MODID + ":dalek"));
    public static final RegistrySupplier<EntityType<Kanine>> KANINE = ENTITY_TYPES.register("k9", () -> EntityType.Builder.of(Kanine::new, MobCategory.CREATURE).sized(0.9F, 0.9F).build(MODID + ":k9"));

    public static <T extends Entity> RegistrySupplier<EntityType<T>> register(String id, Supplier<EntityType.Builder<T>> builderSupplier) {
        return ENTITY_TYPES.register(id, () -> builderSupplier.get().build(MODID + ":" + id));
    }
}
