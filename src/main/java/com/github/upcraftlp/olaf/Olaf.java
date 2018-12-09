package com.github.upcraftlp.olaf;

import com.github.upcraftlp.glasspane.api.proxy.IProxy;
import com.github.upcraftlp.glasspane.util.ModUpdateHandler;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import static com.github.upcraftlp.olaf.Olaf.*;

@Mod(
        certificateFingerprint = FINGERPRINT_KEY,
        name = MODNAME,
        version = VERSION,
        acceptedMinecraftVersions = MCVERSIONS,
        modid = MODID,
        dependencies = DEPENDENCIES,
        updateJSON = UPDATE_JSON
)
public class Olaf {

    //Version
    public static final String MCVERSIONS = "[1.12.2,1.13)";
    public static final String VERSION = "@VERSION@";

    //Meta Information
    public static final String MODNAME = "Olaf";
    public static final String MODID = "olaf";
    public static final String DEPENDENCIES = "required-after:glasspane;required-after:forge";
    public static final String UPDATE_JSON = "@UPDATE_JSON@";

    public static final String FINGERPRINT_KEY = "@FINGERPRINTKEY@";

    @SidedProxy(clientSide = "com.github.upcraftlp.olaf.proxy.ClientProxy", serverSide = "com.github.upcraftlp.olaf.proxy.ServerProxy")
    public static IProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModUpdateHandler.registerMod(MODID);
        proxy.preInit(event);
    }

}
