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

    public boolean isChildVisible(Component c) {
        return c.getRectX() <= getRectW() && c.getRectY() <= getRectH();
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void pointerPressed(int x, int y) {
        if (!isPointerEventTransparent()) return;
        x -= getRectX();
        y -= getRectY();
        Component c;
        for (int i = 0; i < getChildrenCount(); i++) {
            c = getChildAt(i);
            if (c.getRectX() <= x && x <= c.getRectX() + c.getRectW() &&
                c.getRectY() <= y && y <= c.getRectY() + c.getRectH())
                c.pointerPressed(x, y);
        }
    }

    public void pointerReleased(int x, int y) {
        if (!isPointerEventTransparent()) return;
        x -= getRectX();
        y -= getRectY();
        Component c;
        for (int i = 0; i < getChildrenCount(); i++) {
            c = getChildAt(i);
            if (c.getRectX() <= x && x <= c.getRectX() + getRectW() &&
                c.getRectY() <= y && y <= c.getRectY() + getRectH())
                c.pointerReleased(x, y);
        }
    }

    public void pointerDragged(int x, int y) {
        if (!isPointerEventTransparent()) return;
        x -= getRectX();
        y -= getRectY();
        Component c;
        for (int i = 0; i < getChildrenCount(); i++) {
            c = getChildAt(i);
            if (c.getRectX() <= x && x <= c.getRectX() + getRectW() &&
                c.getRectY() <= y && y <= c.getRectY() + getRectH())
                c.pointerDragged(x, y);
        }
    }

}
