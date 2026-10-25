package me.tapeline.cuteui.components;

import me.tapeline.cuteui.EMath;
import me.tapeline.cuteui.Event;
import me.tapeline.cuteui.layout.Layout;

import javax.microedition.lcdui.Graphics;

public class VScrollPanel extends Panel {

	private static final byte IDLE = 0;
	private static final byte TOUCHING = 1;
	private static final byte SCROLLING = 2;
	private static final byte FORWARDING = 3;
	private static final byte KEEPING_MOMENTUM = 4;
	
	private static final double FRICTION_COEFF = 0.7;
	private static final double STOP_MARGIN = 0.01;

    private int handleColor = 0x333333;

    private int yScroll = 0;
    private byte state = IDLE;
    private int lastX;
    private int lastY;
    private long lastMs;
	private double instantSpeed = 0.0;
	
	private double currentSpeed = 0.0;
	private long lastTickMs;

    public VScrollPanel(Layout layout) {
        super(layout);
    }

    public void paint(Graphics g) {
        if (getRectW() <= 0 || getRectH() <= 0) return;
        paintSelf(g);
        paintChildren(g);
        paintHandle(g);
		if (DEBUG) paintDebug(g);
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
        int viewportH = getRectH() - 2 * getMargin();
		int measuredH = getMeasuredH() - 2 * getMargin();
        int viewportPercent = viewportH * 1000 / measuredH;
        int handleH = viewportPercent * viewportH / 1000;
        int handleY = yScroll * viewportPercent / 1000 + getRectY() + getMargin();
        g.setColor(handleColor);
        g.fillRoundRect(
            getRectX() + getRectW() - getMargin() - 4,
            handleY,
            4,
            handleH,
            2,
            2
        );
    }
	
	protected void paintDebug(Graphics g) {
		g.drawString(Double.toString(instantSpeed), 10, 10, Graphics.TOP | Graphics.LEFT);
		g.drawString(Integer.toString(state), 10, 20, Graphics.TOP | Graphics.LEFT);
		g.drawString(Double.toString(currentSpeed), 10, 30, Graphics.TOP | Graphics.LEFT);
	}

    public byte pointerPressed(int x, int y) {
        lastX = x;
        lastY = y;
		lastMs = System.currentTimeMillis();
		state = TOUCHING;
        return super.pointerPressed(x, y + yScroll);
    }

    public byte pointerReleased(int x, int y) {
        if (state == SCROLLING) {
			currentSpeed = instantSpeed;
			state = KEEPING_MOMENTUM;
            return Event.CONSUMED;
        } else {
    		state = IDLE;
            lastX = x;
            lastY = y;
	        return super.pointerReleased(x, y + yScroll);
		}
    }

    public byte pointerDragged(int x, int y) {
		long ms = System.currentTimeMillis();
		
		if (state == FORWARDING) {
            lastX = x;
            lastY = y;
            return super.pointerDragged(x, y + yScroll);
        }
			
		if (state == TOUCHING) {
			// do we, or our children scroll?
			if (super.pointerDragged(lastX, lastY + yScroll) == Event.CONSUMED) {
				state = FORWARDING;
                lastX = x;
                lastY = y;
				return Event.CONSUMED;
			} else {
				state = SCROLLING;
				int lx = x - getRectX();
				int ly = y - getRectY();
                Component child;
				for (int i = 0; i < getChildrenCount(); i++) {
                    child = getChildAt(i);
                    if (lastX >= child.getRectX() && lastX <= child.getRectX() + child.getRectW() &&
                        lastY + yScroll >= child.getRectY() && lastY + yScroll <= child.getRectY() + child.getRectH())
                        getChildAt(i).pointerReleasedOutside(lx, ly);
				}
			}
		}
		
		if (state == SCROLLING) {
	        int dy = y - lastY;
			double dt = ms - lastMs;
	        int scrollCode = setScroll(yScroll - dy);
			instantSpeed = ((double) dy) / dt;
            lastX = x;
            lastY = y;
			lastMs = System.currentTimeMillis();
	        return Event.CONSUMED;
		}
		
        return Event.IGNORED;
    }

    public void tick() {
        if (state == KEEPING_MOMENTUM) {
			long ms = System.currentTimeMillis();
			System.out.println(ms - lastMs);
			if (ms - lastMs > 100) {
				lastMs = ms;
				return;
			}
			int dy = (int) ((long) (currentSpeed * ((double) ms - lastMs)));
			if (setScroll(yScroll - dy) != 0) {
                currentSpeed = 0;
                state = IDLE;
                return;
            }
			currentSpeed = currentSpeed * FRICTION_COEFF;
			if (Math.abs(currentSpeed) < 0.01) {
				currentSpeed = 0;
				state = IDLE;
			}
		}
    }

    public int getScroll() {
        return yScroll;
    }

    public int setScroll(int yScroll) {
        int inset = getMargin() + getPadding();
        int innerH = getRectH() - 2 * inset;
        int contentH = getMeasuredH() - 2 * inset;
        int maxScroll = contentH - innerH;
        if (maxScroll < 0) maxScroll = 0;
		int code = 0;
        if (yScroll < 0) {
			yScroll = 0;
			code = -1;
		}
        if (yScroll > maxScroll) {
			yScroll = maxScroll; 
			code = 1;
		}
        if (this.yScroll == yScroll)
			return code;
        this.yScroll = yScroll;
        invalidatePaint();
		return code;
    }

    public int getHandleColor() {
        return handleColor;
    }

    public void setHandleColor(int handleColor) {
        this.handleColor = handleColor;
        invalidatePaint();
    }

}
