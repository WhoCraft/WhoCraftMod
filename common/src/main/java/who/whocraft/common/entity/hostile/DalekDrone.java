package who.whocraft.common.entity.hostile;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DalekDrone extends AbstractDalek{


    public enum DalekDroneState {
        IDLE,
        SCAN,
        PURSUE,
        CHARGE,
        ATTACK
    }

    Goal meleeAttackGoal = new MeleeAttackGoal(this, 0.5f, true);
    Goal nearestAttackGoal = new NearestAttackableTargetGoal<>(this, Player.class, true);
    private DalekDroneState attackState;
    LivingEntity attackTarget = null;
    private int attackTime = 0;
    private int attackCooldown = 0;
    private int ammo = 3;

    public DalekDrone(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        attackState = DalekDroneState.IDLE;
    }

    @Override
    protected @NotNull
    PathNavigation createNavigation(@NotNull Level worldIn) {
        WallClimberNavigation navigator = new WallClimberNavigation(this, worldIn);
        navigator.setCanFloat(true);
        navigator.setCanOpenDoors(true);
        navigator.setAvoidSun(false);
        navigator.setSpeedModifier(1.0D);
        return navigator;
    }

    @Override
    public void tick() {
        super.tick();

        if (!level.isClientSide()) {
            if (tickCount % 20 == 0) {
                if (attackCooldown > 0) {
                    attackCooldown--;
                }
            }

            if (attackTarget != null) {
                Vec3 vecPos = position();
                Vec3 vecPlayerPos = attackTarget.position();
                float angle = (float) Math.toDegrees((float) Math.atan2(vecPos.z - vecPlayerPos.z, vecPos.x - vecPlayerPos.x));
                yHeadRot = yBodyRot = angle > 180 ? angle : angle + 90;
            }
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!level.isClientSide()) {

            // Update state.
            if (attackState == DalekDroneState.IDLE) {
                onIdleTick();
                return;
            }

            if (attackState == DalekDroneState.PURSUE) {
                onPursueTick();
                return;
            }

            if (attackState == DalekDroneState.ATTACK) {
                onAttackTick();
                return;
            }
        }
    }



    public void onIdleTick() {
        List<Player> players = level.getEntitiesOfClass(Player.class, getBoundingBox().inflate(20));
        players.removeIf(player -> player.isSpectator() || player.isInvisible() || player.level != level);

        if (!players.isEmpty()) {
            System.out.println("Found player, changing state.");
            players.forEach(x -> x.displayClientMessage(Component.translatable("Dalek found player, switching to Pursue"), false));
            setTarget(players.get(level.random.nextInt(players.size())));
            beginPursue();
        }
    }

    public void onPursueTick() {
        if (getTarget() == null) {
            System.out.println("Dalek has no target to pursue.");
        } else {

            if (getTarget().distanceTo(this) < 10 && attackCooldown == 0) {
                beginAttack(getTarget());
            }

        }
    }

    private void onAttackTick() {
        if (ammo < 0) {
            ammo = 3;
            setTarget(attackTarget);
            beginPursue();
            attackCooldown = 5;
            return;
        }

        if (attackTarget!= null) {
            // Attack interval
            if (tickCount % 20 == 0) {
                attackTime--;
                if (attackTime == 0) {
                    System.out.println("FIRE!!!");
                    ProjectileUtil.getHitResult(this, (test) -> {
                        System.out.println(test);
                        return false;
                    });
                    ammo--;
                    attackTime = 3;
                }
            }
        }
    }

    private void beginAttack(LivingEntity entity) {
        Player target = (Player) getTarget();
        this.attackTarget = entity;
        target.displayClientMessage(Component.translatable("Dalek is beginning attack!"), false);
        attackState = DalekDroneState.ATTACK;
        goalSelector.removeGoal(nearestAttackGoal);
        goalSelector.removeGoal(meleeAttackGoal);
        attackTime = 3;
        getNavigation().stop();
        setTarget(null);
        setDeltaMovement(0,0,0);
    }

    private void beginPursue() {
        attackState = DalekDroneState.PURSUE;
        goalSelector.addGoal(2, nearestAttackGoal);
        goalSelector.addGoal(2, meleeAttackGoal);

    }
}
