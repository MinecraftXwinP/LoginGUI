package minecraftxwinp.logingui.common.gui;

import cpw.mods.fml.common.launcher.FMLTweaker;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.relauncher.FMLLaunchHandler;
import minecraftxwinp.logingui.common.LoginGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Created by Minecraft on 2014/5/18.
 */
public class GuiLogin extends GuiScreen {

    private Minecraft mc;
    //information
    private final String playerName;
    private final World world;
    //components
    private TextFieldLogin textField;
    private GuiButton pwdMem;
    private GuiCheckbox checkbox;
    //flags
    private boolean memPwd;

    //default password for textfield
    //background
    private ResourceLocation bgLocation;
    public GuiLogin(String playerName, World theWorld)
    {
        this.playerName = playerName;
        this.world = theWorld;
        mc = Minecraft.getMinecraft();
        memPwd = LoginGUI.config.get(Configuration.CATEGORY_GENERAL,"Memory Passworld", false).getBoolean(false);
        theWorld.playSoundAtEntity(mc.thePlayer,"logingui.entry",1,1);
    }

    @Override
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        buttonList.clear();
        buttonList.add(0, new GuiButton(0, width - 25 - 80, height - 30 - 20, 80, 20, "登入"));
        pwdMem = new GuiButton(1,width - 25 - 85 - 100,height - 30 - 20,100,20,"記錄密碼:" + (memPwd ? "On" : "Off"));
        buttonList.add(1, pwdMem);
        textField = new TextFieldLogin(fontRenderer,25,height - 30 - 20,width - 240,20);
        if (memPwd)
            textField.setDefaultPassword(LoginGUI.config.get(Configuration.CATEGORY_GENERAL,"password","").getString());
        checkbox = new GuiCheckbox(240,160,30);
        textField.setFocused(false);
        try {
            BufferedImage background = ImageIO.read(new File(LoginGUI.getDirPath() + System.getProperty("file.separator") + "background.png"));
            bgLocation = mc.renderEngine.getDynamicTextureLocation("xploginbg", new DynamicTexture(background));
        } catch (IOException ex)
        {
            bgLocation = null;
            ex.printStackTrace();
        }
    }
    @Override
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }
    @Override
    public void updateScreen()
    {
        textField.updateCursorCounter();
    }

    @Override
    public void actionPerformed(GuiButton button)
    {
        switch (button.id)
        {
            case 0:
                sendMessageAction();
                break;
            case 1:
                memPwd = !memPwd;
                LoginGUI.config.get(Configuration.CATEGORY_GENERAL,"Memory Passworld", false).set(memPwd);
                LoginGUI.config.save();
                break;
        }

    }
    @Override
    public void drawScreen(int para1,int para2,float para3)
    {
        //draw the background
        //TODO  draw custom gui background
        if (bgLocation != null)
        {
            mc.renderEngine.bindTexture(bgLocation);
            Tessellator ts = Tessellator.instance;
            ts.startDrawingQuads();
            ts.addVertexWithUV(20, 20, this.zLevel, 0, 0);//bottom left texture
            ts.addVertexWithUV(20, height - 20, this.zLevel, 0, 1);//top left
            ts.addVertexWithUV(width - 20, height - 20, this.zLevel, 1, 1);//top right
            ts.addVertexWithUV(width - 20, 20, this.zLevel, 1, 0);//bottom right
            ts.draw();
        } else
        {
            int dy = fontRenderer.getCharWidth('a') * 2;
            int dx = fontRenderer.getStringWidth("背景圖片遺失");
            for (int i = 0; i < (width - 40) / dx; i++)
            {
                for (int j = 0; j < (height - 40) / (dy + 5); j++)
                {
                    fontRenderer.drawString("背景圖片遺失", 20 + i * dx, 20 + j * dy, 0xFFFFFF);
                }
            }

        }
        textField.drawTextBox();
//        checkbox.drawGui();
        /*
                    Note: String,x,y,color,dropShadow
                 */
        fontRenderer.drawString("您好! " + playerName, 25,height - 30 - 20 - 40,0xFFFFFF);
        fontRenderer.drawString("當前世界時間 : " + worldTimeToSting() ,25, height - 30 - 20 - 20, 0xFFFFFF);
        pwdMem.displayString = "記錄密碼:" + (memPwd ? "On" : "Off");
        super.drawScreen(para1, para2, para3);
    }
    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    public void keyTyped(char key,int i)
    {
        if (!textField.isDisplayDefaultMessage() && i == Keyboard.KEY_RETURN && textField.getText().length() > 0)
            sendMessageAction();
        textField.textboxKeyTyped(key,i);
        super.keyTyped(key,i);
    }

    @Override
    //Doesn't sure what parameter is x,y and button autally
    public void mouseClicked(int x,int y,int button)
    {
        super.mouseClicked(x,y,button);
        textField.mouseClicked(x,y,button);
    } 
    private void sendMessageAction()
    {
        //if the textField is still displaying default message, don't send the message
        if(textField.isDisplayDefaultMessage())
            return;
        if (memPwd)
        {
            LoginGUI.config.get(Configuration.CATEGORY_GENERAL, "password","").set(textField.getText());
            LoginGUI.config.save();
        }
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/login " + textField.getText());
//        LoginGUI.getLogger().log(Level.INFO,System.getProperty("user.dir"));
        this.mc.displayGuiScreen(null);
    }
    //generate the string that represent the time of the world
    private String worldTimeToSting()
    {
        long worldTime = world.getWorldTime() - 6000;
        int DAY = 24000;
        int MINUTE = 167;
        int day = ((int)worldTime / DAY);
        long _min = ((int)(worldTime/MINUTE));
        int hour = (int)(_min / 60 % 24);

        return day + " 天 " + hour + " 時 " + _min % 60 + " 分 ";
    }


}
