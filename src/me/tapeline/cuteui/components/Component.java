package me.tapeline.cuteui.components;

import me.tapeline.cuteui.Consts;

import javax.microedition.lcdui.Graphics;

public abstract class Component {

    private int minW = Consts.UNDEFINED;
    private int minH = Consts.UNDEFINED;
    private int maxW = Consts.UNDEFINED;
    private int maxH = Consts.UNDEFINED;

    // Maybe split into 4?
    private int padding = 0;
    private int margin = 0;

    private int measuredW, measuredH;

    private int rectW, rectH, rectX, rectY;

    private boolean layoutValid = false;
    private boolean paintValid = false;

    private boolean focused = false;
    private boolean enabled = true;

    private Component parent = null;

    public abstract void measure(int maxW, int maxH);

    public void layout() {
    }

    public abstract void paint(Graphics g);

    public void keyPressed(int key) {
    }

    public void keyReleased(int key) {
    }

    public void keyHeld(int key) {
    }

    public void pointerPressed(int x, int y) {
    }

    public void pointerReleased(int x, int y) {
    }

    public void pointerDragged(int x, int y) {
    }

    public void invalidateLayout() {
        layoutValid = false;
        paintValid = false;
        if (parent != null) parent.invalidateLayout();
    }

    public void invalidatePaint() {
        paintValid = false;
        if (parent != null) parent.invalidatePaint();
    }

    public void setMeasurementResult(int w, int h) {
        measuredW = w;
        measuredH = h;
    }

    public int getMeasuredW() {
        return measuredW;
    }

    public int getMeasuredH() {
        return measuredH;
    }

    public void setCalculatedRect(int x, int y, int w, int h) {
        rectX = x;
        rectY = y;
        rectW = w;
        rectH = h;
    }

    public int getRectX() {
        return rectX;
    }

    public int getRectY() {
        return rectY;
    }

    public int getRectW() {
        return rectW;
    }

    public int getRectH() {
        return rectH;
    }

    public boolean isLayoutValid() {
        return layoutValid;
    }

    public boolean isPaintValid() {
        return paintValid;
    }

    public void markLayoutValid() {
        layoutValid = true;
    }

    public void markPaintValid() {
        paintValid = true;
    }

    public Component getParent() {
        return parent;
    }

    public void setParent(Component parent) {
        this.parent = parent;
    }

    public int getMinW() {
        return minW;
    }

    public void setMinW(int minW) {
        this.minW = minW;
        invalidateLayout();
    }

    public int getMinH() {
        return minH;
    }

    public void setMinH(int minH) {
        this.minH = minH;
        invalidateLayout();
    }

    public int getMaxW() {
        return maxW;
    }

    public void setMaxW(int maxW) {
        this.maxW = maxW;
        invalidateLayout();
    }

    public int getMaxH() {
        return maxH;
    }

    public void setMaxH(int maxH) {
        this.maxH = maxH;
        invalidateLayout();
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
        invalidateLayout();
    }

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
        invalidateLayout();
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
        invalidatePaint();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        invalidatePaint();
    }

}