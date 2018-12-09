package com.github.upcraftlp.olaf.proxy;

import com.github.upcraftlp.glasspane.api.proxy.IProxy;
import com.github.upcraftlp.olaf.entity.EntityIceBall;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class ClientProxy implements IProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityIceBall.class, (renderManager) -> new RenderSnowball<>(renderManager, Item.getItemFromBlock(Blocks.ICE), Minecraft.getMinecraft().getRenderItem()));
    }
}
