package me.tapeline.cuteui.components;

import me.tapeline.cuteui.layout.Layout;

import javax.microedition.lcdui.Graphics;

public class VScrollPanel extends Panel {

    private int yScroll = 0;
    private boolean isTouching = false;
    private boolean isScrolling = false;
    private int pressX, pressY;
    private int lastY;
    private int handleColor = 0x333333;

    private static final int SCROLL_THRESHOLD = 4;

    public VScrollPanel(Layout layout) {
        super(layout);
    }

    public void paint(Graphics g) {
        if (getRectW() <= 0 || getRectH() <= 0) return;
        paintSelf(g);
        paintChildren(g);
        paintHandle(g);
        markPaintValid();
    }

    protected void paintChildren(Graphics g) {
        int oldClipX = g.getClipX();
        int oldClipY = g.getClipY();
        int oldClipW = g.getClipWidth();
        int oldClipH = g.getClipHeight();

        int inset = getMargin() + getPadding();
        int innerX = getRectX() + inset;
        int innerY = getRectY() + inset;
        int innerW = getRectW() - 2 * inset;
        int innerH = getRectH() - 2 * inset;

        if (innerW <= 0 || innerH <= 0) return;
        g.clipRect(innerX, innerY, innerW, innerH);
        int tx = getRectX();
        int ty = getRectY() - yScroll;
        g.translate(tx, ty);

        for (int i = 0; i < getChildrenCount(); i++) {
            Component c = getChildAt(i);
            int cy = c.getRectY() - yScroll;
            if (cy + c.getRectH() <= 0 || cy >= getRectH()) continue;
            c.paint(g);
        }

        g.translate(-tx, -ty);
        g.setClip(oldClipX, oldClipY, oldClipW, oldClipH);
    }

    protected void paintHandle(Graphics g) {
        int fullH = getRectH() - 2 * getMargin();
        int vpPercentage = getRectH() * 1000 / getMeasuredH();
        int handleH = vpPercentage * fullH / 1000;
        int handleY = yScroll * vpPercentage / 1000 + getRectY() + getMargin();
        g.setColor(handleColor);
        if (handleH + handleY > getRectH() - getMargin())
            handleH = getRectH() - getMargin() - handleY;
        g.fillRect(
            getRectX() + getRectW() - getMargin() - 4,
            handleY ,
            4, handleH
        );
    }

    public boolean pointerPressed(int x, int y) {
        pressX = x;
        pressY = y;
        lastY = y;
        isTouching = true;
        isScrolling = false;
        return true;
    }

    public boolean pointerReleased(int x, int y) {
        if (!isTouching) return false;
        isTouching = false;
        if (isScrolling) {
            isScrolling = false;
            return true;
        }
        int sx = pressX;
        int sy = pressY + yScroll;
        boolean handled = super.pointerPressed(sx, sy);
        handled = super.pointerReleased(sx, sy) || handled;
        return handled;
    }

    public boolean pointerDragged(int x, int y) {
        if (!isTouching) return false;
        int dy = y - lastY;
        if (!isScrolling) {
            if (Math.abs(y - pressY) < SCROLL_THRESHOLD &&
                Math.abs(x - pressX) < SCROLL_THRESHOLD) {
                return true;
            }
            isScrolling = true;
        }
        setScroll(yScroll - dy);
        lastY = y;
        return true;
    }

    public int getScroll() {
        return yScroll;
    }

    public void setScroll(int yScroll) {
        int inset = getMargin() + getPadding();
        int innerH = getRectH() - 2 * inset;
        int contentH = getMeasuredH() - 2 * inset;
        int maxScroll = contentH - innerH;
        if (maxScroll < 0) maxScroll = 0;
        if (yScroll < 0) yScroll = 0;
        if (yScroll > maxScroll) yScroll = maxScroll;
        if (this.yScroll == yScroll) return;
        this.yScroll = yScroll;
        invalidatePaint();
    }

    public int getHandleColor() {
        return handleColor;
    }

    public void setHandleColor(int handleColor) {
        this.handleColor = handleColor;
        invalidatePaint();
    }

}
