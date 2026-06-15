package me.tapeline.cuteui.test;

import me.tapeline.cuteui.UI;
import me.tapeline.cuteui.components.Label;
import me.tapeline.cuteui.components.Panel;
import me.tapeline.cuteui.components.VScrollPanel;
import me.tapeline.cuteui.layout.HLayout;
import me.tapeline.cuteui.layout.VLayout;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Font;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class CuteUITest extends MIDlet {

    public CuteUITest() {
        UI.start();
        Panel a = new VScrollPanel(new VLayout(4));
        a.setMargin(16);
        a.setPadding(4);
        Panel b = new Panel(new HLayout(4));
        b.setBackgroundColor(0xEEEEEE);
        b.setPadding(4);
        b.addChild(new Label("Hello"));
        b.addChild(new Label("This is the\nupper panel"));
        Panel c = new Panel(new VLayout(4));
        c.setBackgroundColor(0xEEEEEE);
        c.setPadding(4);
        c.addChild(new Label("Hello again"));
        c.addChild(new Label("This is the\nmiddle panel"));
        Label l = new Label("This is a label with paddings and margins. Click me") {
            public boolean pointerPressed(int x, int y) {
                if (getPadding() != 0) setPadding(0);
                else setPadding(4);
                return true;
            }
        };
        l.setPadding(4);
        l.setMargin(4);
        a.addChild(b);
        a.addChild(c);
        a.addChild(l);
        Label l2 = new Label("This is a customised text");
        l2.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_LARGE));
        l2.setColor(0xFF00FF);
        a.addChild(l2);
        a.addChild(new Label("This is another label"));
        a.addChild(new Label("This is another label"));
        a.addChild(new Label("This is another label"));
        a.addChild(new Label("This is another label"));
        a.addChild(new Label("This is another label"));
        UI.instance.setRoot(a);
    }

    protected void startApp() throws MIDletStateChangeException {
        Display.getDisplay(this).setCurrent(UI.instance);
        UI.instance.getRoot().invalidateLayout();
    }

    protected void pauseApp() {

    }

    protected void destroyApp(boolean b) throws MIDletStateChangeException {

    }

}
