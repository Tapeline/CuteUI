package me.tapeline.cuteui.layout;

import me.tapeline.cuteui.Consts;
import me.tapeline.cuteui.components.Component;
import me.tapeline.cuteui.components.Container;

public class HLayout implements Layout {

    private int gap;

    public HLayout() {
        this(0);
    }

    public HLayout(int gap) {
        this.gap = gap;
    }

    public void measure(Container c, int maxW, int maxH) {
        int w = (c.getPadding() + c.getMargin()) * 2;
        int h = 0;
        int childrenCount = c.getChildrenCount();
        int childMaxH = maxH - (c.getMargin() + c.getPadding()) * 2;
        if (maxH == Consts.UNDEFINED) childMaxH = Consts.UNDEFINED;
        Component child;
        for (int i = 0; i < childrenCount; i++) {
            child = c.getChildAt(i);
            child.measure(Consts.UNDEFINED, childMaxH);
            w += child.getMeasuredW();
            h = Math.max(child.getMeasuredH(), h);
        }
        h += (c.getPadding() + c.getMargin()) * 2;
        w += (childrenCount - 1) * gap;
        c.setMeasurementResult(w, Math.max(maxH, h));
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
            x += gap + child.getMeasuredW();
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
