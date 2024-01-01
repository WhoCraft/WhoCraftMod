package who.whocraft.common.entity.hostile;

import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import who.whocraft.common.WhocraftSound;

import java.awt.*;
import java.util.List;

public class DalekDrone extends AbstractDalek implements RangedAttackMob, GeoEntity {


    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    protected static final RawAnimation FLY_ANIM = RawAnimation.begin().thenLoop("dalek.idle");


    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Flying", 5, this::flyAnimController));
    }

    private PlayState flyAnimController(AnimationState<DalekDrone> dalekDroneAnimationState) {
        if (!dalekDroneAnimationState.isMoving()) {
            return dalekDroneAnimationState.setAndContinue(FLY_ANIM);
        }
        return PlayState.STOP;
    }

    @Override
    public void baseTick() {
        super.baseTick();
        triggerAnim("Flying", "idle");
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    @Override
    public double getTick(Object object) {

        if(object instanceof Entity entity){
            return entity.tickCount;
        }

        return 0;
    }

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

    private LivingEntity clientSideCachedAttackTarget;

    Vec3 targetPlacement = null;

    private static final EntityDataAccessor<Boolean> FIRING = SynchedEntityData.defineId(DalekDrone.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_ID_ATTACK_TARGET = SynchedEntityData.defineId(DalekDrone.class, EntityDataSerializers.INT);

    public DalekDrone(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.goalSelector.addGoal(0, new FloatGoal(this));
        speechCooldownSeconds = 2 + level().getRandom().nextInt(13);
        beginIdle();
        SingletonGeoAnimatable.registerSyncedAnimatable(this);

    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(FIRING, false);
        getEntityData().define(DATA_ID_ATTACK_TARGET, 0);
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

        if (!level().isClientSide()) {
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
        dialogueRandomTick();

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
                if (level().getRandom().nextInt(3) == 0) {
                    if (!level().isClientSide()) {
                        level().playSound(null, this, WhocraftSound.DALEK_EXTERMINATE.get(), SoundSource.HOSTILE, 1.25f, 1f);
                    }
                    speechCooldownSeconds = 5 + level().getRandom().nextInt(10);
                }
            }
        }
    }

    public void onIdleTick() {
        if (!level().isClientSide()) {
            List<Player> players = level().getEntitiesOfClass(Player.class, getBoundingBox().inflate(20));
            players.removeIf(player -> player.isSpectator() || player.isInvisible() || player.level() != level() || player.isCreative());

            if (!players.isEmpty()) {
                updateEntityTarget(players.get(level().random.nextInt(players.size())));
                beginPursue();
            }
        }
    }

    public void onPursueTick() {
        if (getTarget() == null) {
            beginIdle();
        } else {
            if ((getTarget().distanceTo(this) < 25 && hasLineOfSight(getTarget())) && attackCooldown == 0) {
                beginAttack(getTarget());
            }
        }
    }

    private void updateEntityTarget(LivingEntity target) {
        if (target == null) {
            this.getEntityData().set(DATA_ID_ATTACK_TARGET, 0);
            setTarget(null);
        } else {
            this.getEntityData().set(DATA_ID_ATTACK_TARGET, target.getId());
            setTarget(target);
        }


    }

    private void onAttackTick() {

        getNavigation().stop();
        setSpeed(0);
        setDeltaMovement(0, 0, 0);
        //setTarget(attackTarget);
        this.getNavigation().stop();
        this.getLookControl().setLookAt(attackTarget, 90f, 90f);
        this.setYBodyRot(getYHeadRot());

        if (ammo <= 0) {
            ammo = 3;

            updateEntityTarget(attackTarget);

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
                    if (laserTime == 0 || !this.hasLineOfSight(attackTarget)) {
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
                    if (attackTarget.getDeltaMovement().horizontalDistance() < 0.1) {
                    //TODO    attackTarget.hurt(new DamageSource(DamageTypes.CACTUS, this), 1f);
                    }

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
//                //this.level().setBlock(new BlockPos(targetPlacement), Blocks.IRON_BLOCK.defaultBlockState(), 3);
////                HitResult hit = this.level().clip(new ClipContext(thisPos, targetPlacement.add(0,1,0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
////                System.out.println(hit.getType());
////                //this.level().setBlock(new BlockPos(hit.getLocation()), Blocks.GLOWSTONE.defaultBlockState(), 3);
//////                if (result != null) {
//////                    System.out.println("Found " + result.getEntity().getName());
//////                }
////
////                double d = (double)blockPosition().getX() + 0.5D;
////                double e = (double)blockPosition().getY() + 0.7D;
////                double f = (double)blockPosition().getZ() + 0.5D;
////                level().addParticle(ParticleTypes.SMOKE, d, e, f, 0.0D, 0.0D, 0.0D);
////                level().addParticle(ParticleTypes.FLAME, d, e, f, 0.0D, 0.0D, 0.0D);
////
////                if (hit.getType() == HitResult.Type.ENTITY) {
////                    System.out.println("AHHHH");
////                }
//            }
        }


    }

    private void beginAttack(LivingEntity entity) {
        announceAlerted();
        Player target = (Player) getTarget();
        this.attackTarget = entity;
        target.displayClientMessage(Component.translatable("Dalek is beginning attack!"), false);
        attackState = DalekDroneState.ATTACK;
        this.getBrain().removeAllBehaviors();
        this.goalSelector.removeGoal(lookAroundGoal);
        getNavigation().stop();
        setSpeed(0);
        setDeltaMovement(0, 0, 0);
        goalSelector.addGoal(0, new LookAtPlayerGoal(this, Player.class, 30f));
        attackTime = 3;
        laserTime = 2;

        if (!level().isClientSide()) {
            level().playSound(null, this, WhocraftSound.DALEK_GUN_CHARGE.get(), SoundSource.HOSTILE, 1f, 1f);
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
        updateEntityTarget(null);
    }

    private void announceAlerted() {
        if (attackState == DalekDroneState.IDLE) {
            if (!level().isClientSide()) {
                level().playSound(null, this, WhocraftSound.DALEK_ALERT.get(), SoundSource.HOSTILE, 1.25f, 1f);
            }
        }
    }

    @Override
    public void performRangedAttack(LivingEntity livingEntity, float amount) {
        //TODO Temp code for testing - thanks Craig
        AbstractArrow arrow = ProjectileUtil.getMobArrow(this, new ItemStack(Items.ARROW), 99);
        double dX = livingEntity.getX() - this.getX();
        double dY = livingEntity.getY(0.3333333333333333) - arrow.getY();
        double dZ = livingEntity.getZ() - this.getZ();
        double distance = Math.sqrt(dX * dX + dZ * dZ);
        arrow.shoot(dX, dY + distance * 0.2, dZ, 2.0F, (float) (14 - this.level().getDifficulty().getId() * 4));
        this.playSound(WhocraftSound.DALEK_GUN_FIRE.get(), 5.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(arrow);

    }

    public boolean getIsFiring() {
        return getEntityData().get(FIRING);
    }

    public LivingEntity getActiveAttackTarget() {
        Entity entity = this.level().getEntity(this.entityData.get(DATA_ID_ATTACK_TARGET));
        System.out.println(entity);
        if (entity instanceof LivingEntity) {
            return (LivingEntity) entity;
        } else {
            return null;
        }
    }

}
