package me.tapeline.cuteui.components;

import me.tapeline.cuteui.Consts;

import javax.microedition.lcdui.Graphics;

public class Button extends Label {

    private int command;
    private boolean pressed = false;
    private CommandListener listener = null;

    private int passiveBackground = 0x444444;
    private int activeBackground = 0x1144EE;
    private int cornerRadius = 16;

    private long releaseMs = 0;

    public Button(String text, int command) {
        super(text);
        this.command = command;
        setColor(0xFCFCFC);
        setAlignment(Consts.ALIGN_CENTER);
        setPadding(2);
    }

    public void tick() {
        if (releaseMs < 0) return;
        if (System.currentTimeMillis() > releaseMs) {
            releaseMs = -1;
            pressed = false;
            invalidatePaint();
        }
    }

    public void paint(Graphics g) {
        if (pressed) {
            g.setColor(activeBackground);
        } else {
            g.setColor(passiveBackground);
        }
        g.fillRoundRect(
            getRectX() + getMargin(),
            getRectY() + getMargin(),
            getRectW() - 4 * getMargin(),
            getRectH() - 4 * getMargin(),
            cornerRadius,
            cornerRadius
        );
        super.paint(g);
    }

    public boolean pointerPressed(int x, int y) {
        pressed = true;
        // kinda sketchy, fix later
        releaseMs = System.currentTimeMillis() + 100;
        invalidatePaint();
        return true;
    }

    public boolean pointerReleased(int x, int y) {
        invalidatePaint();
        if (listener != null) listener.onCommand(command);
        return true;
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

    public int getPassiveBackground() {
        return passiveBackground;
    }

    public void setPassiveBackground(int passiveBackground) {
        this.passiveBackground = passiveBackground;
        invalidatePaint();
    }

    public int getActiveBackground() {
        return activeBackground;
    }

    public void setActiveBackground(int activeBackground) {
        this.activeBackground = activeBackground;
        invalidatePaint();
    }

    public int getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        invalidatePaint();
    }

}
