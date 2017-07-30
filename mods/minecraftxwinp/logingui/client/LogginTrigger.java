package minecraftxwinp.logingui.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import minecraftxwinp.logingui.common.LoginGUI;
import minecraftxwinp.logingui.common.gui.GuiLogin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

/**
 * Created by Minecraft on 2014/5/21.
 */
public class LogginTrigger {
    @ForgeSubscribe(priority = EventPriority.NORMAL)
    public void onTriggered(ClientChatReceivedEvent event)
    {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
            Minecraft.getMinecraft().getLogAgent().logInfo(event.message);
            if (event.message.contains("§f§f§e§f"))
            Minecraft.getMinecraft().displayGuiScreen(new GuiLogin(Minecraft.getMinecraft().thePlayer.getDisplayName(),
                                                        Minecraft.getMinecraft().theWorld));
        }
    }
}
