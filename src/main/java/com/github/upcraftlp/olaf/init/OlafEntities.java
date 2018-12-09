package com.github.upcraftlp.olaf.init;

import com.github.upcraftlp.glasspane.api.registry.AutoRegistry;
import com.github.upcraftlp.olaf.Olaf;
import com.github.upcraftlp.olaf.entity.EntityIceBall;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.*;

@SuppressWarnings("unused")
@AutoRegistry(Olaf.MODID)
public class OlafEntities {

    public static final EntityEntry ICE_BALL = EntityEntryBuilder.create()
            .entity(EntityIceBall.class)
            .id(new ResourceLocation(Olaf.MODID, "ice_ball"), 0)
            .name("olaf.ice_ball")
            .factory(EntityIceBall::new)
            .tracker(64, 10, true)
            .build();
}
