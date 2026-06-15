package me.tapeline.cuteui;

import me.tapeline.cuteui.components.Component;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import java.lang.Runnable;
import java.lang.Thread;

public class UI extends Canvas implements Runnable {

    private static final int TICK_MS = 20;

    private int backplateColor = 0xCCCCCC;
    private Component root;

    public static UI instance = null;

    public static void start() {
        instance = new UI();
        instance.running = true;
        instance.thread = new Thread(instance);
        instance.thread.start();
    }

    public static void stop() {
        instance.running = false;
        instance.thread.interrupt();
        try {
            instance.thread.join();
        } catch (InterruptedException ignored) {
        }
        instance = null;
    }

    private boolean running = true;
    private Thread thread = null;

    public void run() {
        while (running) {
            if (root != null) {
                if (!root.isLayoutValid()) {
                    root.setMinW(getWidth());
                    root.setMinH(getHeight());
                    root.setMaxW(getWidth());
                    root.setMaxH(getHeight());
                    root.measure(getWidth(), getHeight());
                    root.setCalculatedRect(0, 0, getWidth(), getHeight());
                    root.layout();
                }
                if (!root.isPaintValid()) {
                    repaint();
                    serviceRepaints();
                }
            }
            try {
                Thread.sleep(TICK_MS);
            } catch (InterruptedException ignored) {
            }
        }
    }

    protected void sizeChanged(int w, int h) {
        root.invalidateLayout();
    }

    public void paint(Graphics g) {
        g.setColor(backplateColor);
        g.fillRect(0, 0, getWidth(), getHeight());
        if (root != null) {
            if (!root.isPaintValid())
                root.paint(g);
        }
    }

    public int getBackplateColor() {
        return backplateColor;
    }

    public void setBackplateColor(int backplateColor) {
        this.backplateColor = backplateColor;
    }

    public void keyPressed(int k) {
        if (root != null)
            root.keyPressed(k);
    }

    public void keyReleased(int k) {
        if (root != null)
            root.keyReleased(k);
    }

    public void keyRepeated(int k) {
        if (root != null)
            root.keyHeld(k);
    }

    public void pointerPressed(int x, int y) {
        if (root != null)
            root.pointerPressed(x, y);
    }

    public void pointerReleased(int x, int y) {
        if (root != null)
            root.pointerReleased(x, y);
    }

    public void pointerDragged(int x, int y) {
        if (root != null)
            root.pointerDragged(x, y);
    }

    public Component getRoot() {
        return root;
    }

    public void setRoot(Component root) {
        this.root = root;
    }

}