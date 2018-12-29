package com.github.upcraftlp.olaf.item;

import com.github.upcraftlp.glasspane.item.ItemBase;
import com.github.upcraftlp.olaf.entity.EntityIceBall;
import com.google.common.collect.Multimap;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.*;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.*;

import javax.annotation.Nullable;
import java.util.*;

public class ItemIceStaff extends ItemBase {

    public ItemIceStaff() {
        super("ice_staff");
        this.setCreativeTab(CreativeTabs.COMBAT);
        this.setFull3D();
        this.setMaxDamage(100);
        this.setNoRepair();
        this.addPropertyOverride(new ResourceLocation("minecraft", "cooldown"), (stack, worldIn, entityIn) -> 1.0F); //cancel out the attack cooldown animation
    }

    /**
     * called when right-clicking an entity
     */
    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        if(playerIn.getCooldownTracker().hasCooldown(this)) return true;
        if(target != null) {
            target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 65, 3));
            playerIn.getCooldownTracker().setCooldown(this, 78);
            return true;
        }
        return false;
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
        return true; //cancel the swing animation
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> map = super.getAttributeModifiers(slot, stack);
        if(slot == EntityEquipmentSlot.MAINHAND || slot == EntityEquipmentSlot.OFFHAND) {
            map.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", 4.0D, 0));
        }
        return map;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        if(!player.getCooldownTracker().hasCooldown(this) && entity instanceof EntityLivingBase) {
            ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 65, 3));
            ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 14, 4));
            player.getCooldownTracker().setCooldown(this, 93);
        }
        return true;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return newStack.getItem() != this; //oldStack#getItem() is ALWAYS 'this'
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if(!worldIn.isRemote) {
            if(!stack.hasTagCompound()) {
                stack.setTagCompound(new NBTTagCompound());
                stack.getTagCompound().setString("FireMode", FireMode.SINGLE.name());
            }
            FireMode mode = FireMode.valueOf(stack.getTagCompound().getString("FireMode"));
            if(playerIn.isSneaking()) {
                mode = mode.cycle();
                stack.getTagCompound().setString("FireMode", mode.name());
                playerIn.sendStatusMessage(new TextComponentTranslation("message.olaf.staff_mode_changed", new TextComponentTranslation(mode.getTranslationKey())).setStyle(new Style().setColor(TextFormatting.GREEN)), true);
            }
            else {
                playerIn.getCooldownTracker().setCooldown(this, mode.shoot(worldIn, playerIn, handIn, stack));
                stack.damageItem(1, playerIn);
            }
        }
        //if shifting: cycle to multi-shot mode
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        FireMode mode = stack.hasTagCompound() && stack.getTagCompound().hasKey("FireMode", Constants.NBT.TAG_STRING) ? FireMode.valueOf(stack.getTagCompound().getString("FireMode")) : FireMode.SINGLE;
        tooltip.add(I18n.format("item.olaf.ice_staff.desc", I18n.format(mode.getTranslationKey())));
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return toRepair.getItem() == this && toRepair.getItemDamage() != 0 && repair.getItem() == Item.getItemFromBlock(Blocks.ICE);
    }

    public enum FireMode {
        SINGLE((world, player, hand, stack) -> {
            EntityIceBall iceBall = new EntityIceBall(world, player);
            iceBall.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 2.5F, 0.0F);
            world.spawnEntity(iceBall);
            return 16; //0.8 sec
        }),
        BURST((world, player, hand, stack) -> {
            for(int i = 0; i < 2 + itemRand.nextInt(3); i++) {
                EntityIceBall iceBall = new EntityIceBall(world, player);
                iceBall.shoot(player, player.rotationPitch, player.rotationYaw, 0.1F, 1.8F, 10.4F);
                world.spawnEntity(iceBall);
            }
           return 65; //3.25 sec
        });

        private final FireAction action;
        FireMode(FireAction action) {
            this.action = action;
        }

        public int shoot(World world, EntityPlayer player, EnumHand hand, ItemStack stack) {
            return this.action.shoot(world, player, hand, stack);
        }

        public FireMode cycle() {
            return values().length > this.ordinal() + 1 ? values()[this.ordinal() + 1] : values()[0];
        }

        public String getTranslationKey() {
            return "item.olaf.staff_mode." + this.name().toLowerCase(Locale.ROOT);
        }
    }

    @FunctionalInterface
    public interface FireAction {
        int shoot(World world, EntityPlayer player, EnumHand hand, ItemStack stack);
    }
}
