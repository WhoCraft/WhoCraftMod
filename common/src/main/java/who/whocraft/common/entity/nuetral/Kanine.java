package who.whocraft.common.entity.nuetral;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Kanine extends TamableAnimal implements RangedAttackMob {


    public AnimationState EARS = new AnimationState();
    public AnimationState POWERED_DOWN = new AnimationState();


    public Kanine(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    @Override //TODO This is horrible
    public @NotNull AttributeMap getAttributes() {
        return new AttributeMap(createAttributes().build());
    }


    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.1));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(3, new RangedAttackGoal(this, 1.0, 20, 15.0F));
        //   this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true));

    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    public void tick() {
        super.tick();


        tickAnimations();

        if (level.isClientSide()) {
            if (isDisabled()) {
                this.level.addParticle(ParticleTypes.SMOKE, this.getRandomX(0.5), this.getRandomY(), this.getRandomZ(0.5), 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected boolean isImmobile() {
        return isDisabled() || super.isDeadOrDying();
    }

    private void tickAnimations() {

        if (isDisabled()) {
            if (!POWERED_DOWN.isStarted()) {
                EARS.stop();
                POWERED_DOWN.start(tickCount);
            }
            return;
        }

        if (!EARS.isStarted()) {
            POWERED_DOWN.stop();
            EARS.start(tickCount);
        }

    }

    public boolean isDisabled() {
        return getHealth() == 1;
    }

    @Override
    public void performRangedAttack(LivingEntity livingEntity, float amount) {
        //TODO Temp code for testing
        ItemStack itemStack = this.getProjectile(this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, Items.BOW)));
        AbstractArrow abstractArrow = ProjectileUtil.getMobArrow(this, itemStack, 99);
        double d = livingEntity.getX() - this.getX();
        double e = livingEntity.getY(0.3333333333333333) - abstractArrow.getY();
        double g = livingEntity.getZ() - this.getZ();
        double h = Math.sqrt(d * d + g * g);
        abstractArrow.shoot(d, e + h * 0.20000000298023224, g, 1.6F, (float) (14 - this.level.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(abstractArrow);
    }

    @Override /*If K9 is at critical health, stop damage to allow player repairs*/
    public boolean hurt(DamageSource damageSource, float f) {

        if (getHealth() - f <= 0 && isDisabled()) {
            setHealth(1);
            return false;
        }

        if (!isDisabled() || damageSource == DamageSource.OUT_OF_WORLD) {
            return super.hurt(damageSource, f);
        }

        return false;
    }

    /*=== Redundancy ===*/

    /*Disable Pushing*/
    public boolean isPushable() {
        return false;
    }

    /*Disable Pushing*/
    protected void doPush(Entity entity) {
    }

    /*Disable Pushing*/
    protected void pushEntities() {
    }

    @Nullable
    @Override /*K9 cannot breed due to being a robot dog*/
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }


    @Override /*K9 cannot breed due to being a robot dog*/
    public boolean canBreed() {
        return false;
    }

}
