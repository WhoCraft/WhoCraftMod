package who.whocraft.common.entity.hostile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import who.whocraft.common.entity.bodycontroller.DalekBodyController;

import java.util.List;

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
                add(Attributes.MOVEMENT_SPEED, 0.8D).
                add(Attributes.ARMOR, 5.0D);
    }


    @Override
    public void aiStep() {
        super.aiStep();

//        if (!level.isClientSide()) {
//            List<Player> players = level.getEntitiesOfClass(Player.class, getBoundingBox().inflate(30));
//            players.removeIf(player -> player.isSpectator() || player.isInvisible() || player.level != level);
//
//            if (!players.isEmpty()) {
//                Player targetPlayer = players.get(level.getRandom().nextInt(players.size()));
//                setTarget(targetPlayer);
//
//            }
//
//            if (getTarget() != null) {
//                moveToLivingEntity(getTarget());
//            }
//
//        }

    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new DalekBodyController(this);
    }

    /**
     * Move towards a targeted Enemy.
     * **/
    public void setMovementTargetPosition(Vec3 position) {
        getNavigation().moveTo(position.x(), position.y(), position.z(), getSpeed());
    }

    public void moveToLivingEntity(LivingEntity entity) {
        getNavigation().moveTo(entity, getSpeed());
    }

}
