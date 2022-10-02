package who.whocraft.common.entity.hostile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.WardenRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.monster.warden.WardenAi;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import who.whocraft.common.WhocraftSound;
import who.whocraft.common.entity.WhocraftEntity;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.function.Predicate;

public class DalekDrone extends AbstractDalek implements RangedAttackMob {

    public enum DalekDroneState {
        IDLE, SCAN, PURSUE, CHARGE, ATTACK
    }

    // Goals
    Goal meleeAttackGoal = new MeleeAttackGoal(this, 0.5f, true);
    Goal nearestAttackGoal = new NearestAttackableTargetGoal<>(this, Player.class, true);
    Goal nearestAttackGoalVillager = new NearestAttackableTargetGoal<>(this, Villager.class, true);
    Goal lookAroundGoal = new RandomLookAroundGoal(this);
    Goal randomWalkGoal = new WaterAvoidingRandomStrollGoal(this, getSpeed());

    public Color LASER_COLOR = Color.BLUE;

    private DalekDroneState attackState;
    LivingEntity attackTarget = null;
    private int attackTime = 0;
    private int attackCooldown = 0;
    private int laserTime = 0;
    private int ammo = 3;
    private int speechCooldownSeconds;

    Vec3 targetPlacement = null;

    private static final EntityDataAccessor<Boolean> FIRING = SynchedEntityData.defineId(DalekDrone.class, EntityDataSerializers.BOOLEAN);

    public DalekDrone(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.goalSelector.addGoal(0, new FloatGoal(this));
        speechCooldownSeconds = 2 + level.getRandom().nextInt(13);
        beginIdle();

    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(FIRING, false);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level worldIn) {
        WallClimberNavigation navigator = new WallClimberNavigation(this, worldIn);
        navigator.setCanFloat(true);
        navigator.setCanOpenDoors(true);
        navigator.setAvoidSun(false);
        navigator.setSpeedModifier(0.5D);
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
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        // Update state.
        if (speechCooldownSeconds > 0 && tickCount % 20 == 0) {speechCooldownSeconds--;}
        //dialogueRandomTick();

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

    public void dialogueRandomTick(){
        if (attackState == DalekDroneState.ATTACK) {
            if (speechCooldownSeconds == 0) {
                if (level.getRandom().nextInt(3) == 0) {
                    if (!level.isClientSide()) {
                        level.playSound(null, this, WhocraftSound.DALEK_EXTERMINATE.get(), SoundSource.HOSTILE, 1.25f, 1f);
                    }
                    speechCooldownSeconds = 5 + level.getRandom().nextInt(10);
                }
            }
        }
    }

    public void onIdleTick() {
        if (!level.isClientSide()) {
            List<Player> players = level.getEntitiesOfClass(Player.class, getBoundingBox().inflate(20));
            players.removeIf(player -> player.isSpectator() || player.isInvisible() || player.level != level || player.isCreative());

            if (!players.isEmpty()) {
                setTarget(players.get(level.random.nextInt(players.size())));
                beginPursue();
            }
        }
    }

    public void onPursueTick() {
        if (getTarget() == null) {
            beginIdle();
        } else {
            if ((getTarget().distanceTo(this) < 10 || hasLineOfSight(getTarget())) && attackCooldown == 0) {
                beginAttack(getTarget());
            }
        }
    }

    private void onAttackTick() {

        getNavigation().stop();
        setSpeed(0);
        setDeltaMovement(0, 0, 0);
        setTarget(attackTarget);

        if (ammo <= 0) {
            ammo = 3;

            setTarget(attackTarget);

            attackCooldown = 3;
            beginPursue();
            return;
        }

        if (attackTarget != null) {
            // Attack interval
            if (tickCount % 20 == 0) {
                attackTime--;
            }

            if (tickCount % 10 == 0 ) {
                if (attackTime <= 0) {
                    if (laserTime == 0) {
                        attackTime = 1;
                        laserTime = 2;
                        getEntityData().set(FIRING, false);
                        System.out.println("Stopped firing");
                        ammo--;
                    } else {
                        laserTime--;
                        if (!getIsFiring()) {
                            targetPlacement = attackTarget.position();
                            this.playSound(WhocraftSound.DALEK_GUN_FIRE.get(), 5.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
                        }
                        getEntityData().set(FIRING, true);

                    }
                }
            }

            if (getIsFiring()) {
                if (this.hasLineOfSight(attackTarget)) {
                    System.out.println("CAN SEE PLAYER");
                    attackTarget.hurt(DamageSource.CACTUS, 1f);

                }
            }

            //System.out.println(hit.getType());

//
//            if (getIsFiring()) {
//                var thisPos = position().add(0, 1, 0);
//                var attackPos = attackTarget.position();
//                var result = findHitEntity(thisPos, attackPos);
//
//
//                if (result != null) {
//                    System.out.println(result.getEntity().getDisplayName());
//                }
//
//                //this.level.setBlock(new BlockPos(targetPlacement), Blocks.IRON_BLOCK.defaultBlockState(), 3);
////                HitResult hit = this.level.clip(new ClipContext(thisPos, targetPlacement.add(0,1,0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
////                System.out.println(hit.getType());
////                //this.level.setBlock(new BlockPos(hit.getLocation()), Blocks.GLOWSTONE.defaultBlockState(), 3);
//////                if (result != null) {
//////                    System.out.println("Found " + result.getEntity().getName());
//////                }
////
////                double d = (double)blockPosition().getX() + 0.5D;
////                double e = (double)blockPosition().getY() + 0.7D;
////                double f = (double)blockPosition().getZ() + 0.5D;
////                level.addParticle(ParticleTypes.SMOKE, d, e, f, 0.0D, 0.0D, 0.0D);
////                level.addParticle(ParticleTypes.FLAME, d, e, f, 0.0D, 0.0D, 0.0D);
////
////                if (hit.getType() == HitResult.Type.ENTITY) {
////                    System.out.println("AHHHH");
////                }
//            }
        }
    }

    protected EntityHitResult findHitEntity(Vec3 vec3, Vec3 vec32) {
        return ProjectileUtil.getEntityHitResult(this.level, this, vec3, vec32, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(10.0D), this::canHitEntity);
    }

    protected boolean canHitEntity(Entity entity) {
        return true;
    }

    private void beginAttack(LivingEntity entity) {
        announceAlerted();
        Player target = (Player) getTarget();
        this.attackTarget = entity;
        target.displayClientMessage(Component.translatable("Dalek is beginning attack!"), false);
        attackState = DalekDroneState.ATTACK;
        this.getBrain().removeAllBehaviors();
        getNavigation().stop();
        setSpeed(0);
        setDeltaMovement(0, 0, 0);
        goalSelector.addGoal(0, new LookAtPlayerGoal(this, Player.class, 30f));
        attackTime = 3;
        laserTime = 2;

        if (!level.isClientSide()) {
            level.playSound(null, this, WhocraftSound.DALEK_GUN_CHARGE.get(), SoundSource.HOSTILE, 1f, 1f);
        }
    }

    private void beginPursue() {
        announceAlerted();
        attackState = DalekDroneState.PURSUE;
        goalSelector.addGoal(2, nearestAttackGoal);
        goalSelector.addGoal(2, meleeAttackGoal);
        goalSelector.removeGoal(lookAroundGoal);
        goalSelector.removeGoal(randomWalkGoal);

    }

    private void beginIdle() {
        this.goalSelector.addGoal(8, lookAroundGoal);
        this.goalSelector.addGoal(6, randomWalkGoal);
        attackState = DalekDroneState.IDLE;
        setTarget(null);
    }

    private void announceAlerted() {
        if (attackState == DalekDroneState.IDLE) {
            if (!level.isClientSide()) {
                level.playSound(null, this, WhocraftSound.DALEK_ALERT.get(), SoundSource.HOSTILE, 1.25f, 1f);
            }
        }
    }

    @Override
    public void performRangedAttack(LivingEntity livingEntity, float amount) {
        //TODO Temp code for testing - thanks Craig
        ItemStack itemStack = this.getProjectile(this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, Items.BOW)));
        AbstractArrow abstractArrow = ProjectileUtil.getMobArrow(this, itemStack, 99);
        double d = livingEntity.getX() - this.getX();
        double e = livingEntity.getY(0.3333333333333333) - abstractArrow.getY();
        double g = livingEntity.getZ() - this.getZ();
        double h = Math.sqrt(d * d + g * g);
        abstractArrow.shoot(d, e + h * 0.20000000298023224, g, 2F, (float) (14 - this.level.getDifficulty().getId() * 4));
        this.playSound(WhocraftSound.DALEK_GUN_FIRE.get(), 5.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(abstractArrow);
    }

    public boolean getIsFiring() {
        return getEntityData().get(FIRING);
    }

}
