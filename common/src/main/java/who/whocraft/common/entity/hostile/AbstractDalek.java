package who.whocraft.common.entity.hostile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractDalek extends Monster implements Enemy {

    protected AbstractDalek(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public @NotNull AttributeMap getAttributes() {
        return new AttributeMap(createAttributes().build());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMonsterAttributes().
                add(Attributes.ATTACK_DAMAGE, 8D).
                add(Attributes.MAX_HEALTH, 20D).
                add(Attributes.KNOCKBACK_RESISTANCE, 0.5D).
                add(Attributes.MOVEMENT_SPEED, 0.3D).
                add(Attributes.ARMOR, 5.0D);
    }

    public void onAiServerStep() {

    }

    @Override
    public void aiStep() {

    }

    /**
     * Move towards a targeted Enemy.
     * **/
    public void setMovementTargetPosition(BlockPos position) {
        getNavigation().moveTo(position.getX(), position.getY(), position.getZ(), getSpeed());
    }

}
