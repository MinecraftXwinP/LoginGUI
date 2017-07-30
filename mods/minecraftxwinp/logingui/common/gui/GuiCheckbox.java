package minecraftxwinp.logingui.common.gui;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Minecraft on 2014/6/3.
 */
public class GuiCheckbox extends Gui {

    private int xPos;
    private int yPos;
    private int width;
    private int height;
    // width of leap of the gui
    private double leap = 0.8;


    private List<ActionListener> listenerList;

    public GuiCheckbox(int x,int y,int width,int height)
    {
        xPos = x;
        yPos = y;
        this.width = width;
        this.height = height;

        listenerList = new LinkedList<ActionListener>();
    }

    public GuiCheckbox(int x,int y,int arc)
    {
        this(x,y,arc,arc);
    }

    public void drawGui()
    {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Tessellator ts = Tessellator.instance;
        ts.startDrawingQuads();
        ts.setColorRGBA(205,255,255,255);
        ts.addVertex(xPos - leap, yPos - height - leap, 0);
        ts.addVertex(xPos - leap, yPos - leap, 0);
        ts.addVertex(xPos + width - leap, yPos - leap,0);
        ts.addVertex(xPos + width - leap, yPos - height - leap, 0);
        ts.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);

    }

    public void registerActionLitener(ActionListener listener)
    {
        listenerList.add(listener);
    }

    private void acknowledgeListeners()
    {
        ListIterator<ActionListener> iterator = listenerList.listIterator();
        while(iterator.hasNext())
        {
            ActionEvent e = new ActionEvent(this,0,null);
            iterator.next().actionPerformed(e);
        }
    }

}
