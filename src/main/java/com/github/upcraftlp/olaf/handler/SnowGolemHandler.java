package com.github.upcraftlp.olaf.handler;

import com.github.upcraftlp.glasspane.api.util.*;
import com.github.upcraftlp.glasspane.api.util.serialization.datareader.*;
import com.github.upcraftlp.olaf.Olaf;
import com.github.upcraftlp.olaf.config.OlafConfig;
import com.github.upcraftlp.olaf.entity.ai.EntityAIAttackRangedIceBall;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.*;

import java.util.*;

@Mod.EventBusSubscriber(modid = Olaf.MODID)
public class SnowGolemHandler {

    private static final String COLOR_AQUA = "\u00A7b"; //cannot use the client-only text formatting class here
    private static final List<String> SNOWMAN_LINES = Arrays.asList(ForgeUtils.readAssetData(new ResourceLocation(Olaf.MODID, "texts/build_a_snowman.txt"), DataReaders.TEXT, DataReader.AssetType.DATA).split("\r?\n"));
    private static long lastMessage = 0;
    private static final Random RANDOM = new Random();

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onSpawnGolem(EntityJoinWorldEvent event) {
        if(event.getEntity() instanceof EntitySnowman) {
            EntitySnowman snowman = (EntitySnowman) event.getEntity();
            if(!snowman.hasCustomName() && !snowman.world.isRemote) {
                snowman.setCustomNameTag(COLOR_AQUA + "Olaf");
                snowman.setAlwaysRenderNameTag(true);
            }
            snowman.tasks.taskEntries.removeIf(entry -> entry.action.getClass() == EntityAIAttackRanged.class); //*should* be safe as the entity is not yet added to the world
            snowman.tasks.addTask(1, new EntityAIAttackRangedIceBall(snowman, 1.25D, 20, 10.0F));
        }
    }

    @SuppressWarnings("ConstantConditions")
    @SubscribeEvent
    public static void onUpdateSnowGolem(LivingEvent.LivingUpdateEvent event) {
        if(OlafConfig.showTextLines && event.getEntityLiving() instanceof EntitySnowman && event.getEntityLiving().isServerWorld()) {
            EntitySnowman snowman = (EntitySnowman) event.getEntityLiving();
            long currentTime = snowman.world.getTotalWorldTime();
            if(currentTime - lastMessage > OlafConfig.minTextDelay && RANDOM.nextDouble() < OlafConfig.textChance) {
                for(EntityPlayer player : snowman.world.getEntitiesWithinAABB(EntityPlayer.class, snowman.getEntityBoundingBox().grow(5.0D, 2.0D, 5.0D))) {
                    ITextComponent text = new TextComponentString("<").appendSibling(snowman.getDisplayName()).appendText("> ").appendSibling(new TextComponentString(CollectionUtils.getRandomElement(SNOWMAN_LINES)).setStyle(new Style().setItalic(true)));
                    player.sendMessage(text);
                }
                lastMessage = currentTime;
            }
        }
    }
}
