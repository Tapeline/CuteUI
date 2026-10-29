package me.tapeline.cuteui.components;

import me.tapeline.cuteui.core.Event;
import me.tapeline.cuteui.layout.Layout;
import me.tapeline.cuteui.layout.VHLayout;
import me.tapeline.cuteui.layout.VLayout;

import javax.microedition.lcdui.Graphics;

public class ScrollPanel extends Panel {

	private static final byte IDLE = 0;
	private static final byte TOUCHING = 1;
	private static final byte SCROLLING = 2;
	private static final byte FORWARDING = 3;
	private static final byte KEEPING_MOMENTUM = 4;

    // Q16.16: 0.7 * 65536 = 45875
	private static final int FRICTION_COEFF = 45875;
    // Q16.16: 0.01 * 65536 = 655
    private static final int MIN_SPEED_THRESHOLD = 655;

    private int handleColor = 0x333333;

    private boolean vertialEnabled = true;
    private boolean horizontalEnabled = false;

    private int xScroll = 0;
    private int yScroll = 0;
    private byte state = IDLE;
    private int lastX;
    private int lastY;
    private long lastMs;
	private int instantSpeedX = 0;
	private int instantSpeedY = 0;
	private int currentSpeedX = 0;
	private int currentSpeedY = 0;

    public static ScrollPanel verticalOfGap(int gap) {
        ScrollPanel sp = new ScrollPanel(new VLayout(gap));
        sp.setVertialEnabled(true);
        sp.setHorizontalEnabled(false);
        return sp;
    }

    public static ScrollPanel horizontalOfGap(int gap) {
        ScrollPanel sp = new ScrollPanel(new VLayout(gap));
        sp.setVertialEnabled(false);
        sp.setHorizontalEnabled(true);
        return sp;
    }

    public static ScrollPanel mapOfGap(int gap) {
        ScrollPanel sp = new ScrollPanel(new VHLayout(gap));
        sp.setVertialEnabled(true);
        sp.setHorizontalEnabled(true);
        return sp;
    }

    public ScrollPanel(Layout layout) {
        super(layout);
    }

    public void paint(Graphics g) {
        if (getRectW() <= 0 || getRectH() <= 0) return;
        paintSelf(g);
        paintChildren(g);
        if (vertialEnabled) paintVerticalHandle(g);
        if (horizontalEnabled) paintHorizontalHandle(g);
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
        int tx = getRectX() - xScroll;
        int ty = getRectY() - yScroll;
        g.translate(tx, ty);

        for (int i = 0; i < getChildrenCount(); i++) {
            Component c = getChildAt(i);
            int cy = c.getRectY() - yScroll;
            int cx = c.getRectX() - xScroll;
            if (cy + c.getRectH() <= 0 || cy >= getRectH() ||
                cx >= getRectW() || cx + c.getRectW() <= 0) continue;
            c.paint(g);
        }

        g.translate(-tx, -ty);
        g.setClip(oldClipX, oldClipY, oldClipW, oldClipH);
    }

    protected void paintVerticalHandle(Graphics g) {
        int viewportH = getRectH() - 2 * getMargin();
		int measuredH = getMeasuredH() - 2 * getMargin();
        int viewportPercent = viewportH * 1000 / measuredH;
        int handleH = viewportPercent * viewportH / 1000;
        int handleY = yScroll * viewportPercent / 1000 + getRectY() + getMargin();
        g.setColor(handleColor);
        g.fillRoundRect(
            getRectX() + getRectW() - getMargin() - 4, handleY,
            4, handleH,
            2, 2
        );
    }

    protected void paintHorizontalHandle(Graphics g) {
        int viewportW = getRectW() - 2 * getMargin();
        int measuredW = getMeasuredW() - 2 * getMargin();
        int viewportPercent = viewportW * 1000 / measuredW;
        int handleW = viewportPercent * viewportW / 1000;
        int handleX = xScroll * viewportPercent / 1000 + getRectX() + getMargin();
        g.setColor(handleColor);
        g.fillRoundRect(
            handleX, getRectY() + getRectH() - getMargin() - 4,
            handleW, 4,
            2, 2
        );
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
			currentSpeedX = instantSpeedX;
			currentSpeedY = instantSpeedY;
			state = KEEPING_MOMENTUM;
            return Event.CONSUMED;
        } else {
    		state = IDLE;
            lastX = x;
            lastY = y;
	        return super.pointerReleased(x + xScroll, y + yScroll);
		}
    }

    public byte pointerDragged(int x, int y) {
		long ms = System.currentTimeMillis();
		
		if (state == FORWARDING) {
            lastX = x;
            lastY = y;
            return super.pointerDragged(x + xScroll, y + yScroll);
        }
			
		if (state == TOUCHING) {
			// do we, or our children scroll?
			if (super.pointerDragged(lastX + xScroll, lastY + yScroll) == Event.CONSUMED) {
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
                    if (lastX + xScroll >= child.getRectX() &&
                        lastX + xScroll <= child.getRectX() + child.getRectW() &&
                        lastY + yScroll >= child.getRectY() &&
                        lastY + yScroll <= child.getRectY() + child.getRectH())
                        getChildAt(i).pointerReleasedOutside(lx, ly);
				}
			}
		}
		
		if (state == SCROLLING) {
            long dt = ms - lastMs;
            if (vertialEnabled && dt > 0) {
                int dy = y - lastY;
                setYScroll(yScroll - dy);
                // To q16.16
                instantSpeedY = (int) (((long) dy << 16) / dt);
            }
            if (horizontalEnabled && dt > 0) {
                int dx = x - lastX;
                setXScroll(xScroll - dx);
                // To q16.16
                instantSpeedX = (int) (((long) dx << 16) / dt);
            }
            lastX = x;
            lastY = y;
			lastMs = System.currentTimeMillis();
	        return Event.CONSUMED;
		}
		
        return Event.IGNORED;
    }

    public void tick(long t) {
        if (state == KEEPING_MOMENTUM) {
            long dt = t - lastMs;
			if (dt > 100) {
				lastMs = t;
				return;
			}
            if (vertialEnabled) {
                int dy = (int) (((long) currentSpeedY * dt) >> 16);
                if (setYScroll(yScroll - dy) != 0) {
                    currentSpeedY = 0;
                    state = IDLE;
                    return;
                }
                currentSpeedY = (int) (((long) currentSpeedY * FRICTION_COEFF) >> 16);
                if (currentSpeedY < 0 ? currentSpeedY > -MIN_SPEED_THRESHOLD : currentSpeedY < MIN_SPEED_THRESHOLD) {
                    currentSpeedY = 0;
                    state = IDLE;
                }
            }
            if (horizontalEnabled) {
                int dx = (int) (((long) currentSpeedX * dt) >> 16);
                if (setXScroll(xScroll - dx) != 0) {
                    currentSpeedX = 0;
                    state = IDLE;
                    return;
                }
                currentSpeedX = (int) (((long) currentSpeedX * FRICTION_COEFF) >> 16);
                if (currentSpeedX < 0 ? currentSpeedX > -MIN_SPEED_THRESHOLD : currentSpeedX < MIN_SPEED_THRESHOLD) {
                    currentSpeedX = 0;
                    state = IDLE;
                }
            }
		}
    }

    public int getYScroll() {
        return yScroll;
    }

    public int getXScroll() {
        return xScroll;
    }

    public int setYScroll(int yScroll) {
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

    public int setXScroll(int xScroll) {
        int inset = getMargin() + getPadding();
        int innerW = getRectW() - 2 * inset;
        int contentW = getMeasuredW() - 2 * inset;
        int maxScroll = contentW - innerW;
        if (maxScroll < 0) maxScroll = 0;
        int code = 0;
        if (xScroll < 0) {
            xScroll = 0;
            code = -1;
        }
        if (xScroll > maxScroll) {
            xScroll = maxScroll;
            code = 1;
        }
        if (this.xScroll == xScroll)
            return code;
        this.xScroll = xScroll;
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

    public boolean isVertialEnabled() {
        return vertialEnabled;
    }

    public void setVertialEnabled(boolean vertialEnabled) {
        this.vertialEnabled = vertialEnabled;
    }

    public boolean isHorizontalEnabled() {
        return horizontalEnabled;
    }

    public void setHorizontalEnabled(boolean horizontalEnabled) {
        this.horizontalEnabled = horizontalEnabled;
    }

}
