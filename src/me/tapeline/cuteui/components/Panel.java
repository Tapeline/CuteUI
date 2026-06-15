package me.tapeline.cuteui.components;

import me.tapeline.cuteui.layout.Layout;

import javax.microedition.lcdui.Graphics;

public class Panel extends Container {

    private int backgroundColor = 0xFFFFFF;

    public Panel(Layout layout) {
        super(layout);
    }

    protected void paintSelf(Graphics g) {
        g.setColor(backgroundColor);
        g.fillRect(
            getRectX() + getMargin(), getRectY() + getMargin(),
            getRectW() - 2 * getMargin(), getRectH() - 2 * getMargin()
        );
        markPaintValid();
    }

    /**
     * Children's rectangles are stored in coordinates relative to this panel's
     * origin (see {@link me.tapeline.cuteui.layout.Layout}). A child is visible
     * iff its rectangle intersects the panel's inner (content) area, expressed
     * in the same local coordinate system: [0, getRectW()) x [0, getRectH()).
     */
    public boolean isChildVisible(Component c) {
        int cx = c.getRectX();
        int cy = c.getRectY();
        int cw = c.getRectW();
        int ch = c.getRectH();
        return cx + cw > 0 && cy + ch > 0 &&
               cx < getRectW() && cy < getRectH();
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public boolean pointerPressed(int x, int y) {
        if (!isPointerEventTransparent()) return false;
        // Translate to local (child) coordinate space.
        int lx = x - getRectX();
        int ly = y - getRectY();
        Component c;
        for (int i = getChildrenCount() - 1; i >= 0; i--) {
            c = getChildAt(i);
            if (c.getRectX() <= lx && lx <= c.getRectX() + c.getRectW() &&
                c.getRectY() <= ly && ly <= c.getRectY() + c.getRectH()) {
                if (c.pointerPressed(lx, ly)) return true;
            }
        }
        return false;
    }

    public boolean pointerReleased(int x, int y) {
        if (!isPointerEventTransparent()) return false;
        int lx = x - getRectX();
        int ly = y - getRectY();
        Component c;
        for (int i = getChildrenCount() - 1; i >= 0; i--) {
            c = getChildAt(i);
            if (c.getRectX() <= lx && lx <= c.getRectX() + c.getRectW() &&
                c.getRectY() <= ly && ly <= c.getRectY() + c.getRectH()) {
                if (c.pointerReleased(lx, ly)) return true;
            }
        }
        return false;
    }

    public boolean pointerDragged(int x, int y) {
        if (!isPointerEventTransparent()) return false;
        int lx = x - getRectX();
        int ly = y - getRectY();
        Component c;
        for (int i = getChildrenCount() - 1; i >= 0; i--) {
            c = getChildAt(i);
            if (c.getRectX() <= lx && lx <= c.getRectX() + c.getRectW() &&
                c.getRectY() <= ly && ly <= c.getRectY() + c.getRectH()) {
                if (c.pointerDragged(lx, ly)) return true;
            }
        }
        return false;
    }

}
