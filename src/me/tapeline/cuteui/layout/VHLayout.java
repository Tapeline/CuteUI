package me.tapeline.cuteui.layout;

import me.tapeline.cuteui.components.Component;
import me.tapeline.cuteui.components.Container;

public class VHLayout implements Layout {

    private int gap;

    public VHLayout() {
        this(0);
    }

    public VHLayout(int gap) {
        this.gap = gap;
    }

    public void measure(Container c, int maxW, int maxH) {
        int w = 0;
        int h = (c.getPadding() + c.getMargin()) * 2;
        int childrenCount = c.getChildrenCount();
        Component child;
        for (int i = 0; i < childrenCount; i++) {
            child = c.getChildAt(i);
            child.measure(-1, -1);
            w = Math.max(w, child.getMeasuredW());
            h += child.getMeasuredH();
        }
        w += (c.getPadding() + c.getMargin()) * 2;
        h += (childrenCount - 1) * gap;
        c.setMeasurementResult(w, h);
    }

    public void layout(Container c) {
        int x = c.getMargin() + c.getPadding();
        int y = c.getMargin() + c.getPadding();
        int w = c.getMeasuredW() - (c.getMargin() + c.getPadding()) * 2;
        int childrenCount = c.getChildrenCount();
        Component child;
        for (int i = 0; i < childrenCount; i++) {
            child = c.getChildAt(i);
            child.setCalculatedRect(x, y, w, child.getMeasuredH());
            y += gap + child.getMeasuredH();
        }
        c.markLayoutValid();
    }

    public int getGap() {
        return gap;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }

}
