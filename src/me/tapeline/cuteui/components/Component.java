package me.tapeline.cuteui.components;

import me.tapeline.cuteui.core.Event;

import javax.microedition.lcdui.Graphics;

public abstract class Component {

	public static boolean DEBUG = false;

    // Maybe split into 4?
    private int padding = 0;
    private int margin = 0;

    private int measuredW, measuredH;

    private int rectW, rectH, rectX, rectY;

    private boolean layoutValid = false;
    private boolean paintValid = false;

    private Component parent = null;
	
	/*
	The UI loop will once per several tens of milliseconds:
	- trigger tick() on the root
	- if the root does not have a valid layout:
		- measure with display WH
		- layout
	- if the root does not have a valid paint or was re-laid out:
		- repaint
	It is the root component's responsibility to forward these
	calls down the chain.
	*/

    public void tick(long t) {}
    public abstract void measure(int maxW, int maxH);
    public void layout() {}
    public abstract void paint(Graphics g);
	
	/*
	By design, specific events must always follow other.
	CuteUI guarantees that:
	
	- Any X KEY PRESS will always be followed by X KEY RELEASE,
	maybe having 0 or more X KEY HELD in between them.
	
	- Any XY POINTER PRESS will always be followed by any X'Y' 
	POINTER RELEASE, maybe having 0 or more any X'Y' POINTER DRAG
	in between them. 
	
	When a pointer goes out of the component's rect, UI will
	complement POINTER PRESS and POINTER DRAG events with a 
	POINTER RELEASED OUTSIDE event, thus closing the pointer sequence.
	
	POINTER RELEASED OUTSIDE event has a "cancelling" nature,
	it should be primarily used for canceling actions that may have
	had been triggered with POINTER PRESS or POINTER DRAG.
	
	A button example:
	POINTER PRESS -> state = PRESSED
	POINTER RELEASE -> state = NORMAL, trigger action
	POINTER RELEASE OUTSIDE -> state = NORMAL  // no trigger!
	
	Without POINTER RELEASE OUTSIDE, if the user presses a button
	and then drags the finger off, it wouldn't know that it is
	not touched anymore and will live in PRESSED state.
	*/

    public byte keyPressed(int key) {
        return Event.IGNORED;
    }

    public byte keyReleased(int key) {
        return Event.IGNORED;
    }

    public byte keyHeld(int key) {
        return Event.IGNORED;
    }

    public byte pointerPressed(int x, int y) {
        return Event.IGNORED;
    }

    public byte pointerReleased(int x, int y) {
        return Event.IGNORED;
    }

    public byte pointerDragged(int x, int y) {
        return Event.IGNORED;
    }
	
    public byte pointerReleasedOutside(int x, int y) {
        return Event.IGNORED;
    }
	
	/*
	Layout and paint invalidation, when possible,
	will always invalidate the whole hierarchy up to the top
	so that the root knows it needs recalculations and repaints.
	
	When you make a change to a component such that it may
	affect its SIZING or POSITION on the screen, 
	invalidate the layout.
	
	When you make a change to a component such that it may 
	affect its APPEARANCE but NOT the sizing or the position,
	invalidate only the paint.
	*/

    public void invalidateLayout() {
        layoutValid = false;
        paintValid = false;
        if (parent != null) parent.invalidateLayout();
    }

    public void invalidatePaint() {
        paintValid = false;
        if (parent != null) parent.invalidatePaint();
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
	
	/*
	setCalculatedRect(int, int, int, int) should only be called 
	by a Layout.
	setMeasurementResult(int, int) should only be called by
	a Layout or this component during measure() method.
	Using it from other places may result in UI misbehaviour.
	*/

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
	
	/*
	setParent(Component) should only be called by Container
	that is the actual parent of this component.
	Other usage may result in UI misbehaviour.
	*/

    public Component getParent() {
        return parent;
    }

    protected void setParent(Component parent) {
        this.parent = parent;
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
	
	public void onShow() {}
	public void onHide() {}
	public void onDestroy() {}

}