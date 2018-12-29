package com.github.upcraftlp.olaf.init;

import com.github.upcraftlp.glasspane.api.registry.AutoRegistry;
import com.github.upcraftlp.olaf.Olaf;
import com.github.upcraftlp.olaf.item.ItemIceStaff;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

@SuppressWarnings("unused")
@GameRegistry.ObjectHolder(Olaf.MODID)
@AutoRegistry(Olaf.MODID)
public class OlafItems {

    public static final Item ICE_STAFF = new ItemIceStaff();
}
