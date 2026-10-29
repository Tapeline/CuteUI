package me.tapeline.cuteui.components;

import me.tapeline.cuteui.core.Event;
import me.tapeline.cuteui.core.CommandListener;

import javax.microedition.lcdui.Graphics;

public class Button extends Label {

    private int command;
    protected boolean pressed = false;
    private CommandListener listener = null;

    private int baseBackground = 0x242424;
    private int baseBorder = 0x484848;
    private int tint = 0x202020;
    private int cornerRadius = 16;

    public Button(String text, int command) {
        super(text);
        this.command = command;
        setColor(0xFCFCFC);
        setAlignment(Label.ALIGN_CENTER);
        setPadding(4);
    }

    public void paint(Graphics g) {
        g.setColor(pressed ? baseBackground - tint : baseBackground);
        int xs = getRectX() + getMargin();
        int ys = getRectY() + getMargin();
        int cw = getRectW() - 2 * getMargin();
        int ch = getRectH() - 2 * getMargin();
        g.fillRoundRect(xs, ys, cw, ch, cornerRadius, cornerRadius);
        g.setColor(pressed ? baseBorder - tint : baseBorder);
        g.drawRoundRect(xs, ys, cw, ch, cornerRadius, cornerRadius);
        paintLines(g, pressed ? tint : 0);
    }

    public byte pointerPressed(int x, int y) {
        pressed = true;
        invalidatePaint();
        return Event.CONSUMED;
    }

    public byte pointerReleased(int x, int y) {
        pressed = false;
        invalidatePaint();
        if (listener != null) listener.onCommand(command);
        return Event.CONSUMED;
    }

    public byte pointerReleasedOutside(int x, int y) {
        pressed = false;
        invalidatePaint();
        return Event.CONSUMED;
    }

    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public CommandListener getListener() {
        return listener;
    }

    public void setListener(CommandListener listener) {
        this.listener = listener;
    }

    public int getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        invalidatePaint();
    }

}
