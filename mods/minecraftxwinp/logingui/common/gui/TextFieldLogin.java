package minecraftxwinp.logingui.common.gui;

import minecraftxwinp.logingui.common.LoginGUI;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.common.Configuration;

/**
 * Created by Minecraft on 2014/5/21.
 */
/*
    class that extends GuiTextField.
    It displays default message while users haven't done any input.
 */
public class TextFieldLogin extends GuiTextField {
    protected int xPos;
    protected int yPos;
    protected int width;
    protected int height;
    //point out whether the default message should be display.
    private boolean displayDefaultMessage = true;
    private String defaultMessage;
    //focus checking
    private boolean checkFocus;
    private Thread focusCheckThread;
    public TextFieldLogin(FontRenderer par1FontRenderer, int x, int y, int width, int height)
    {
        super(par1FontRenderer,x,y,width,height);
        xPos = x;
        yPos = y;
        this.width = width;
        this.height = height;
        defaultMessage = LoginGUI.config.get(Configuration.CATEGORY_GENERAL,"default message","<請輸入密碼>","message that showed as default text in textfield of Login Gui").getString();
        setTextColor(0x0000FF);
        setCursorPositionZero();
        setText(defaultMessage);
        //focus check
        checkFocus = true;
        focusCheckThread = new Thread(new FocusCheckThread());
        focusCheckThread.start();
    }

    @Override
    public void mouseClicked(int x,int y,int button)
    {
        boolean flag = x >= this.xPos && x < this.xPos + this.width && y >= this.yPos && y < this.yPos + this.height;
        if (flag && displayDefaultMessage)
        {
            if(isFocused() == false)
                onFocusGained();
            setFocused(false);
            setInputMode(false);

        } else
        {
            if(isFocused() == true)
                onFocusLosed();
            setFocused(false);
        }
        super.mouseClicked(x,y,button);
    }

    @Override
    public boolean textboxKeyTyped(char word,int i)
    {
        setFocused(true);
        if (displayDefaultMessage)
        {
            setInputMode(true);
        }

        return super.textboxKeyTyped(word,i);
    }

    public boolean isDisplayDefaultMessage()
    {
        return displayDefaultMessage;
    }

    private void setInputMode(boolean bool)
    {
        if (bool)
        {
            displayDefaultMessage = false;
            setTextColor(0xFFFFFF);
            setText("");
            setCursorPositionZero();
        } else
        {
            displayDefaultMessage = true;
            setTextColor(0x0000FF);
            setText(defaultMessage);
            setCursorPositionZero();
        }
    }
    @Override
    public void updateCursorCounter()
    {
        super.updateCursorCounter();
    }

    class FocusCheckThread implements Runnable
    {
        @Override
        public void run()
        {
            boolean origin = TextFieldLogin.this.isFocused();
            while (checkFocus)
            {
                if (origin && !TextFieldLogin.this.isFocused()) {
                    origin = false;
                    onFocusLosed();
                }
                if (!origin && TextFieldLogin.this.isFocused()) {
                    origin = true;
                    onFocusGained();
                }
                try
                {
                    //delay a while
                    Thread.sleep(800);
                } catch (InterruptedException ex)
                {
                    ex.printStackTrace();
                    break;
                }
            }
        }
    }

    public void onFocusLosed()
    {
        //display default message
        if (displayDefaultMessage)
        {
            setInputMode(false);
        }
    }

    public void onFocusGained()
    {

    }
    //for stopping Focus Checking Thread
//    public void destroy()
//    {
//        checkFocus = false;
//    }

    public void setDefaultPassword(String password)
    {
        setInputMode(true);
        setText(password);
    }
}
