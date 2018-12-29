package com.github.upcraftlp.olaf.entity;

import com.github.upcraftlp.olaf.init.*;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.RecipeBookServer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.*;

public class EntityIceBall extends EntitySnowball {

    public EntityIceBall(World worldIn) {
        super(worldIn);
    }

    public EntityIceBall(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public EntityIceBall(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
    }

    /**
     * Handler for {@link World#setEntityState}
     */
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) {
        if(id == 3) {
            for(int i = 0; i < 8; ++i) {
                //TODO different particle (ice block breaking)
                this.world.spawnParticle(EnumParticleTypes.SNOWBALL, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onImpact(RayTraceResult result) {
        if(result.entityHit == this.thrower && this.ticksExisted < 3) return; //don't hit the player when moving
        if(!this.world.isRemote) {
            if(result.entityHit != null) {
                Entity target = result.entityHit;
                float damage = target instanceof EntityBlaze ? 5.0F : 2.0F;
                if(target instanceof EntityLivingBase) {
                    ((EntityLivingBase) target).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 45, 2));
                    if(target instanceof EntityPlayerMP) {
                        RecipeBookServer recipeBook = ((EntityPlayerMP) target).getRecipeBook();
                        if(!recipeBook.isUnlocked(OlafRecipes.ICE_STAFF)) recipeBook.unlock(OlafRecipes.ICE_STAFF);
                    }
                }
                target.hurtResistantTime = 0;
                target.attackEntityFrom(OlafDamageSources.causeFreezeDamage(this, this.getThrower()), damage);
            }
            this.world.setEntityState(this, (byte) 3); //see handleStatusUpdate()
            this.setDead();
        }
    }
}
