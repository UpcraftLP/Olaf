package com.github.upcraftlp.olaf.handler;

import com.github.upcraftlp.olaf.Olaf;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Olaf.MODID)
public class GolemSpawnHandler {

    private static final String COLOR_AQUA = "\u00A7b"; //cannot use the client-only text formatting class here

    @SubscribeEvent
    public static void onSpawnGolem(EntityJoinWorldEvent event) {
        if(event.getEntity() instanceof EntitySnowman && !event.getEntity().hasCustomName()) {
            event.getEntity().setCustomNameTag(COLOR_AQUA + "Olaf");
            event.getEntity().setAlwaysRenderNameTag(true);
        }
    }
}
