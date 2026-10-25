package me.tapeline.cuteui.components;

import me.tapeline.cuteui.Event;
import me.tapeline.cuteui.layout.Layout;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public abstract class Container extends Component {

    private static final int INITIAL_SIZE = 5;

    private Layout layout;
    private Component[] children = new Component[INITIAL_SIZE];
    private int childrenCount = 0;

    public Container(Layout layout) {
        setLayout(layout);
    }

    public void tick() {
        for (int i = 0; i < childrenCount; i++)
            children[i].tick();
    }

    public void measure(int maxW, int maxH) {
        layout.measure(this, maxW, maxH);
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
		invalidateLayout();
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

    public byte keyPressed(int key) {
        byte status = Event.IGNORED;
		byte childStatus = Event.IGNORED;
        for (int i = 0; i < getChildrenCount(); i++) {
            childStatus = getChildAt(i).keyPressed(key);
			if (childStatus == Event.CONSUMED)
				return Event.CONSUMED;
			else if (childStatus > status)
				status = childStatus;
		}
        return status;
    }

    public byte keyReleased(int key) {
	    byte status = Event.IGNORED;
		byte childStatus = Event.IGNORED;
	    for (int i = 0; i < getChildrenCount(); i++) {
	        childStatus = getChildAt(i).keyReleased(key);
			if (childStatus == Event.CONSUMED)
				return Event.CONSUMED;
			else if (childStatus > status)
				status = childStatus;
		}
	    return status;
    }

    public byte keyHeld(int key) {
	    byte status = Event.IGNORED;
		byte childStatus = Event.IGNORED;
	    for (int i = 0; i < getChildrenCount(); i++) {
	        childStatus = getChildAt(i).keyHeld(key);
			if (childStatus == Event.CONSUMED)
				return Event.CONSUMED;
			else if (childStatus > status)
				status = childStatus;
		}
	    return status;
    }
	
	public void onShow() {
		for (int i = 0; i < getChildrenCount(); i++)
			getChildAt(i).onShow();
	}
	
	public void onHide() {
		for (int i = 0; i < getChildrenCount(); i++)
			getChildAt(i).onHide();
	}
	
	public void onDestroy() {
		for (int i = 0; i < getChildrenCount(); i++)
			getChildAt(i).onDestroy();
		children = null;
		layout = null;
	}

}