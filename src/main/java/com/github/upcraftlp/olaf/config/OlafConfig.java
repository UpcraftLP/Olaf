package com.github.upcraftlp.olaf.config;

import com.github.upcraftlp.olaf.Olaf;
import net.minecraftforge.common.config.*;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config.LangKey("config.olaf.general.title")
@Config(modid = Olaf.MODID, name = "glasspanemods/Olaf") //--> /config/glasspanemods/Olaf.cfg
public class OlafConfig {

    @Config.Name("Enable Text Lines")
    @Config.Comment("Whether or not to show text lines from snowmen occasionally")
    public static boolean showTextLines = true;

    @Config.RangeInt(min = 0)
    @Config.Name("Minimum Text Delay")
    @Config.Comment({"The minimum delay between two text lines, in ticks", "(global for all snowmen, to not spam the chat)"})
    public static int minTextDelay = 100;

    @Config.RangeDouble(min = 0.0D)
    @Config.Name("Text Probability")
    @Config.Comment({"The probability that a text line should be shown this tick", "(applied per entity)"})
    public static double textChance = 0.001D;

    @Config.Name("Shoot Ice")
    @Config.Comment("Whether or not snowmen should be able to attack with ice balls in addition to regular snowballs")
    public static boolean shootIce = true;

    @Config.Name("Ice Ball Probability")
    @Config.Comment("The probability that a snowball will be an ice ball instead")
    public static double iceChance = 0.2D;

    @Mod.EventBusSubscriber(modid = Olaf.MODID)
    public static class Handler {

        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent event) {
            if(Olaf.MODID.equals(event.getModID())) ConfigManager.sync(event.getModID(), Config.Type.INSTANCE);
        }
    }

}
