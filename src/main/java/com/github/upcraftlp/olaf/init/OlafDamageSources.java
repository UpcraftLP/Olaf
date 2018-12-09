package com.github.upcraftlp.olaf.init;

import net.minecraft.entity.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.text.*;
import net.minecraft.util.text.translation.I18n;

import javax.annotation.Nullable;
import java.util.Random;

public class OlafDamageSources {

    private static final Random RANDOM = new Random();
    /**
     * @param entity the entity directly causing damage (i.e. the projectile)
     * @param source the true source of damage (i.e. the shooter/thrower)
     */
    public static DamageSource causeFreezeDamage(Entity entity, @Nullable Entity source) {
        return new FreezeDamageSource("olaf.freeze", entity, source).setProjectile().setDamageBypassesArmor().setDifficultyScaled();
    }

    private static class FreezeDamageSource extends EntityDamageSourceIndirect {

        private static final int DEATH_LINES_COUNT = 7; //how many different translations are there to chose from

        public FreezeDamageSource(String damageTypeIn, Entity source, @Nullable Entity indirectEntityIn) {
            super(damageTypeIn, source, indirectEntityIn);
        }

        @SuppressWarnings({"ConstantConditions", "deprecation"})
        @Override
        public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
            ITextComponent itextcomponent = (this.getTrueSource() == null ? this.damageSourceEntity : this.getTrueSource()).getDisplayName();
            ItemStack itemstack = this.getTrueSource() instanceof EntityLivingBase ? ((EntityLivingBase) this.getTrueSource()).getHeldItemMainhand() : ItemStack.EMPTY;
            String s = "death.attack." + this.damageType + "_" + RANDOM.nextInt(DEATH_LINES_COUNT);
            String s1 = s + ".item";
            return !itemstack.isEmpty() && itemstack.hasDisplayName() && I18n.canTranslate(s1) ? new TextComponentTranslation(s1, entityLivingBaseIn.getDisplayName(), itextcomponent, itemstack.getTextComponent()) : new TextComponentTranslation(s, entityLivingBaseIn.getDisplayName(), itextcomponent);
        }
    }
}
