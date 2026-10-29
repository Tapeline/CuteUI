package me.tapeline.cuteui.core;

import me.tapeline.cuteui.components.Component;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import java.lang.Runnable;
import java.lang.Thread;

public class UI extends Canvas implements Runnable {

    private int backplateColor = 0x000000;
    private Component root;
    private int tickMs = 20;

    public static UI instance = null;

    public void setFps(int fps) {
        tickMs = 1000 / fps;
    }

    public int getFps() {
        return 1000 / tickMs;
    }

    public int getTickMs() {
        return tickMs;
    }

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
        long t, lastT;
        lastT = System.currentTimeMillis();
        while (running) {
            t = System.currentTimeMillis();
            if (root != null) {
                root.tick(t);
                if (!root.isLayoutValid()) {
                    root.measure(getWidth(), getHeight());
                    root.setCalculatedRect(0, 0, getWidth(), getHeight());
                    root.layout();
                }
                if (!root.isPaintValid()) {
                    forceRepaint();
                }
            }
            long adj = System.currentTimeMillis() - lastT;
            lastT = t;
            if (adj >= tickMs) Thread.yield();
            else try {
                Thread.sleep(tickMs - adj);
            } catch (InterruptedException ignored) {
            }
        }
    }

    public void forceRepaint() {
        root.invalidatePaint();
        repaint();
        serviceRepaints();
    }

    protected void sizeChanged(int w, int h) {
        root.invalidateLayout();
        root.measure(w, h);
        root.setCalculatedRect(0, 0, w, h);
        root.layout();
        forceRepaint();
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
