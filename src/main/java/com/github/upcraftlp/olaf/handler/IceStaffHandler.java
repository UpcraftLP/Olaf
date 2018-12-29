package com.github.upcraftlp.olaf.handler;

import com.github.upcraftlp.olaf.Olaf;
import com.github.upcraftlp.olaf.init.*;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.stats.RecipeBookServer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Olaf.MODID)
public class IceStaffHandler {

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if(!event.getWorld().isRemote && event.getItemStack().getItem() == OlafItems.ICE_STAFF) {
            event.setCanceled(true);
            IBlockState state = event.getWorld().getBlockState(event.getPos());
            if(state.getProperties().containsKey(BlockSnow.LAYERS) && state.getValue(BlockSnow.LAYERS) < 8) {
                event.getWorld().setBlockState(event.getPos(), state.withProperty(BlockSnow.LAYERS, state.getValue(BlockSnow.LAYERS) + 1), 22); //flags 2, 4, 16
            }
            else if(event.getFace() == EnumFacing.UP) event.getWorld().setBlockState(event.getPos().offset(event.getFace()), Blocks.SNOW_LAYER.getDefaultState());
        }
    }

    @SubscribeEvent
    public static void onGetBreakSpeed(PlayerEvent.BreakSpeed event) {
        if(event.getEntityPlayer().getHeldItemMainhand().getItem() == OlafItems.ICE_STAFF) event.setCanceled(true);
    }

    @SuppressWarnings("ConstantConditions")
    @SubscribeEvent
    public static void onPickupIce(EntityItemPickupEvent event) {
        if(event.getEntityPlayer() instanceof EntityPlayerMP && event.getItem().getItem().getItem() == Item.getItemFromBlock(Blocks.PACKED_ICE)) {
            RecipeBookServer recipeBook = ((EntityPlayerMP) event.getEntityPlayer()).getRecipeBook();
            if(!recipeBook.isUnlocked(OlafRecipes.ICE_STAFF)) recipeBook.unlock(OlafRecipes.ICE_STAFF);
        }
    }

}
