package com.github.upcraftlp.olaf.handler;

import com.github.upcraftlp.glasspane.api.util.*;
import com.github.upcraftlp.glasspane.api.util.serialization.datareader.*;
import com.github.upcraftlp.olaf.Olaf;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

@Mod.EventBusSubscriber(modid = Olaf.MODID)
public class SnowGolemHandler {

    private static final String COLOR_AQUA = "\u00A7b"; //cannot use the client-only text formatting class here
    private static final List<String> SNOWMAN_LINES = Arrays.asList(ForgeUtils.readAssetData(new ResourceLocation(Olaf.MODID, "texts/build_a_snowman.txt"), DataReaders.TEXT, DataReader.AssetType.DATA).split("\r?\n"));
    private static final long threshold = 100; //FIXME move to config
    private static final double chance = 0.001;
    private static long lastMessage = 0;

    @SubscribeEvent
    public static void onSpawnGolem(EntityJoinWorldEvent event) {
        if(event.getEntity() instanceof EntitySnowman && !event.getEntity().hasCustomName() && !event.getEntity().world.isRemote) {
            event.getEntity().setCustomNameTag(COLOR_AQUA + "Olaf");
            event.getEntity().setAlwaysRenderNameTag(true);
        }
    }

    @SubscribeEvent
    public static void onUpdateSnowGolem(LivingEvent.LivingUpdateEvent event) {
        long currentTime = event.getEntityLiving().world.getTotalWorldTime();
        if(event.getEntityLiving() instanceof EntitySnowman && event.getEntityLiving().isServerWorld() && currentTime - lastMessage > threshold && event.getEntityLiving().getRNG().nextDouble() < chance) {
            event.getEntityLiving().playSound(SoundEvents.ENTITY_SNOWBALL_THROW, 2.0F, 0.4F);
            for(EntityPlayer player : event.getEntityLiving().world.getEntitiesWithinAABB(EntityPlayer.class, event.getEntityLiving().getEntityBoundingBox().grow(5.0D, 2.0D, 5.0D))) {
                ITextComponent text = new TextComponentString("<").appendSibling(event.getEntityLiving().getDisplayName()).appendText("> ").appendSibling(new TextComponentString(CollectionUtils.getRandomElement(SNOWMAN_LINES)).setStyle(new Style().setItalic(true)));
                player.sendMessage(text);
            }
            lastMessage = currentTime;
        }
    }
}
