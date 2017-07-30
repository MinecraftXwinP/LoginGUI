package minecraftxwinp.logingui.common.gui;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by Minecraft on 2014/5/18.
 */
public class GUIHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int modid, EntityPlayer player,World world,int x,int y,int z)
    {
        return null;
    }

    @Override
    public Object getClientGuiElement(int modid, EntityPlayer player,World world,int x,int y,int z)
    {
        return new GuiLogin(player.getDisplayName(), Minecraft.getMinecraft().theWorld);
    }
}
