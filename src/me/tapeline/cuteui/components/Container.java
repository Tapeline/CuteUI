package me.tapeline.cuteui.components;

import me.tapeline.cuteui.layout.Layout;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public abstract class Container extends Component {

    private static final int INITIAL_SIZE = 5;

    private boolean keyEventTransparent = true;
    private boolean pointerEventTransparent = true;
    private Layout layout;
    private Component[] children = new Component[INITIAL_SIZE];
    private int childrenCount = 0;

    public Container(Layout layout) {
        setLayout(layout);
    }

    public void measure(int maxW, int maxH) {
        layout.measure(this, Math.max(maxW, getMaxW()), Math.max(maxH, getMaxH()));
    }

    public void layout() {
        layout.layout(this);
        for (int i = 0; i < childrenCount; i++)
            getChildAt(i).layout();
    }

    protected abstract void paintSelf(Graphics g);

    public abstract boolean isChildVisible(Component c);

    public void paint(Graphics g) {
        paintSelf(g);
        paintChildren(g);
    }

    protected void paintChildren(Graphics g) {
        int cx = g.getClipX();
        int cy = g.getClipY();
        int cw = g.getClipWidth();
        int ch = g.getClipHeight();
        int tx = getRectX();
        int ty = getRectY();
        int inset = getMargin() + getPadding();
        g.clipRect(
            getRectX() + inset,
            getRectY() + inset,
            getRectW() - 2 * inset,
            getRectH() - 2 * inset
        );
        g.translate(tx, ty);
        for (int i = 0; i < childrenCount; i++) {
            if (isChildVisible(children[i])) {
                children[i].paint(g);
            }
        }
        g.translate(-tx, -ty);
        g.setClip(cx, cy, cw, ch);
    }

    public void setLayout(Layout layout) {
        if (layout == null)
            throw new IllegalArgumentException("layout is null");
        this.layout = layout;
    }

    public Layout getLayout() {
        return layout;
    }

    public void addChild(Component c) {
        c.setParent(this);
        if (childrenCount == children.length) {
            Component[] newChildren = new Component[childrenCount / 2 * 3];
            System.arraycopy(children, 0, newChildren, 0, childrenCount);
            children = newChildren;
        }
        children[childrenCount++] = c;
        invalidateLayout();
    }

    public Component[] getChildren() {
        return children;
    }

    public void removeChildAt(int i) {
        children[i].setParent(null);
        if (i + 1 == childrenCount) {
            children[i] = null;
        } else {
            System.arraycopy(children, i + 1, children, i, children.length - i - 1);
        }
        childrenCount--;
        invalidateLayout();
    }

    public void removeChild(Component c) {
        for (int i = 0; i < childrenCount; i++) {
            if (children[i] == c) {
                removeChildAt(i);
                return;
            }
        }
    }

    public void clearChildren() {
        for (int i = 0; i < childrenCount; i++)
            children[i].setParent(null);
        childrenCount = 0;
        children = new Component[INITIAL_SIZE];
        invalidateLayout();
    }

    public int getChildrenCount() {
        return childrenCount;
    }

    public Component getChildAt(int i) {
        if (i >= childrenCount) return null;
        return children[i];
    }

    public boolean isKeyEventTransparent() {
        return keyEventTransparent;
    }

    public void setKeyEventTransparent(boolean keyEventTransparent) {
        this.keyEventTransparent = keyEventTransparent;
    }

    public boolean isPointerEventTransparent() {
        return pointerEventTransparent;
    }

    public void setPointerEventTransparent(boolean pointerEventTransparent) {
        this.pointerEventTransparent = pointerEventTransparent;
    }

    public boolean keyPressed(int key) {
        if (!keyEventTransparent) return false;
        boolean handled = false;
        for (int i = 0; i < getChildrenCount(); i++)
            handled = handled || getChildAt(i).keyPressed(key);
        return handled;
    }

    public boolean keyReleased(int key) {
        if (!keyEventTransparent) return false;
        boolean handled = false;
        for (int i = 0; i < getChildrenCount(); i++)
            handled = handled || getChildAt(i).keyReleased(key);
        return handled;
    }

    public boolean keyHeld(int key) {
        if (!keyEventTransparent) return false;
        boolean handled = false;
        for (int i = 0; i < getChildrenCount(); i++)
            handled = handled || getChildAt(i).keyHeld(key);
        return handled;
    }

}