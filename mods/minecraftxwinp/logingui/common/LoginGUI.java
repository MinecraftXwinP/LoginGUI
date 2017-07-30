package minecraftxwinp.logingui.common;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.launcher.FMLTweaker;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
//import minecraftxwinp.logingui.common.block.DemoBlock;
//import minecraftxwinp.logingui.common.block.info.InfoDemoBlock;
import cpw.mods.fml.relauncher.FMLLaunchHandler;
import minecraftxwinp.logingui.common.gui.GUIHandler;
import minecraftxwinp.logingui.common.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import minecraftxwinp.logingui.client.LogginTrigger;
import net.minecraftforge.transformers.ForgeAccessTransformer;

import java.io.File;
import java.util.StringTokenizer;
import java.util.logging.Logger;

/**
 * Created by Minecraft on 2014/5/17.
 */
/*
    LoginGUI :
        To display a gui for player to enter password, then LoginGUI will pass the password to Server via command line.

      ToDo Crypt Function
 */
@Mod(modid = LoginGUI.MODID, name = LoginGUI.NAME,version = LoginGUI.VERSION)
@NetworkMod(serverSideRequired = false, clientSideRequired = true)
public class LoginGUI {
    public static final String MODID = "xplogingui";
    public static final String NAME = "Login GUI";
    public static final String VERSION = "1.0";
    //logger
    public static Logger logger;
    //login GUI
    private static String dirPath;
    private static File dir;
    @Mod.Instance(MODID)
    public static LoginGUI instance;
    @SidedProxy(serverSide = "minecraftxwinp.logingui.common.proxy.CommonProxy",
            clientSide = "minecraftxwinp.logingui.client.proxy.ClientProxy")
    public static CommonProxy proxy;

    public static Configuration config;

    public String defaultPassword;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        String sp = System.getProperty("file.separator");
        dirPath = config.get(Configuration.CATEGORY_GENERAL,"Mod Path",
                ".minecraft" + sp + "mods" + sp + "xpmod","Directory of Assets").getString();
        config.get(Configuration.CATEGORY_GENERAL,"default message","<請輸入密碼>","message that showed as default text in textfield of Login Gui");
        config.get(Configuration.CATEGORY_GENERAL,"password","");
        config.save();
        LoginGUI.config = config;
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        dir = new File(dirPath);
        //if the folder doesn't exist, create a new one
        if (!dir.exists())
        {
            getLogger().info("xpmod folder doesn't exist,creating a new one");
            if (!dir.mkdirs())
            {
                getLogger().info("Failed to create mod folder");
                throw new RuntimeException("Unable to create mod folder");
            }
        }

//        Block demoBlock = new DemoBlock(InfoDemoBlock.DEFAULT_ID);
//        GameRegistry.registerBlock(demoBlock, InfoDemoBlock.DEFALUT_UNLOCALIZED_NAME);
//        LanguageRegistry.addName(demoBlock, "Demo Block");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        NetworkRegistry.instance().registerGuiHandler(LoginGUI.instance, new GUIHandler());
        MinecraftForge.EVENT_BUS.register(new LogginTrigger());
        StringTokenizer tokenizer = new StringTokenizer(Launch.blackboard.get("ArgumentList").toString(),",");
        while(tokenizer.hasMoreElements())
        {
            if(tokenizer.nextToken().contains("--password"))
            {
                defaultPassword = tokenizer.nextToken();
            }
        }
    }
//    @ForgeSubscribe
//    public void onPlayerLogging(EntityJoinWorldEvent event)
//    {
//        if (!(event.entity instanceof EntityPlayer))
//            return;
//        Minecraft.getMinecraft().displayGuiScreen(new GuiLogin(event.entity.toString(), event.world.getProviderName()));
//        Minecraft.getMinecraft().getLogAgent().logInfo(event.entity.getEntityName());
//        Minecraft.getMinecraft().getLogAgent().logInfo("A_A");
//    }
    public static Logger getLogger()
    {
        if (logger == null)
            throw new RuntimeException("Shouldn't be called here");
        return logger;
    }
    public static String getDirPath()
    {
        return dirPath;
    }
    public static File getDir()
    {
        return dir;
    }
}
