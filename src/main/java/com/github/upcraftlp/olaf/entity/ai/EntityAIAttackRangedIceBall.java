package com.github.upcraftlp.olaf.entity.ai;

import com.github.upcraftlp.olaf.config.OlafConfig;
import com.github.upcraftlp.olaf.entity.EntityIceBall;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.MathHelper;

public class EntityAIAttackRangedIceBall extends EntityAIAttackRanged {

    public EntityAIAttackRangedIceBall(IRangedAttackMob attacker, double movespeed, int maxAttackTime, float maxAttackDistanceIn) {
        super(attacker, movespeed, maxAttackTime, maxAttackDistanceIn);
    }

    @Override
    public void updateTask() {
        double d0 = this.entityHost.getDistanceSq(this.attackTarget.posX, this.attackTarget.getEntityBoundingBox().minY, this.attackTarget.posZ);
        boolean flag = this.entityHost.getEntitySenses().canSee(this.attackTarget);

        if(flag) {
            ++this.seeTime;
        }
        else {
            this.seeTime = 0;
        }

        if(d0 <= (double) this.maxAttackDistance && this.seeTime >= 20) {
            this.entityHost.getNavigator().clearPath();
        }
        else {
            this.entityHost.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);
        }

        this.entityHost.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);

        if(--this.rangedAttackTime == 0) {
            if(!flag) {
                return;
            }

            float f = MathHelper.sqrt(d0) / this.attackRadius;
            if(OlafConfig.shootIce && entityHost.getRNG().nextDouble() < OlafConfig.iceChance) {
                attackWithIceBall((EntityLivingBase) this.rangedAttackEntityHost, this.attackTarget);
            }
            else {
                this.rangedAttackEntityHost.attackEntityWithRangedAttack(this.attackTarget, MathHelper.clamp(f, 0.1F, 1.0F));
            }
            this.rangedAttackTime = MathHelper.floor(f * (float) (this.maxRangedAttackTime - this.attackIntervalMin) + (float) this.attackIntervalMin);
        }
        else if(this.rangedAttackTime < 0) {
            float f2 = MathHelper.sqrt(d0) / this.attackRadius;
            this.rangedAttackTime = MathHelper.floor(f2 * (float) (this.maxRangedAttackTime - this.attackIntervalMin) + (float) this.attackIntervalMin);
        }
    }

    private static void attackWithIceBall(EntityLivingBase source, EntityLivingBase target) {
        EntityIceBall iceBall = new EntityIceBall(source.world, source);
        double d0 = target.posY + (double)target.getEyeHeight() - 1.100000023841858D;
        double d1 = target.posX - source.posX;
        double d2 = d0 - iceBall.posY;
        double d3 = target.posZ - source.posZ;
        float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
        iceBall.shoot(d1, d2 + (double)f, d3, 1.6F, 12.0F);
        source.playSound(SoundEvents.ENTITY_SNOWMAN_SHOOT, 1.0F, 1.0F / (source.getRNG().nextFloat() * 0.4F + 0.8F));
        source.world.spawnEntity(iceBall);
    }
}
