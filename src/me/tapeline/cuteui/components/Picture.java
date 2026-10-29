package me.tapeline.cuteui.components;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Picture extends Component {

    private Image image;

    public Picture(Image image) {
        this.image = image;
    }

    public void measure(int maxW, int maxH) {
        int measuredW = image.getWidth() + 2 * getMargin();
        int measuredH = image.getHeight() + 2 * getMargin();
        setMeasurementResult(
            maxW < 0 ? measuredW : Math.min(maxW, measuredW),
            maxH < 0 ? measuredH : Math.min(maxH, measuredH)
        );
    }

    public void paint(Graphics g) {
        g.drawImage(image, getMargin(), getMargin(), Graphics.TOP | Graphics.LEFT);
        markLayoutValid();
        markPaintValid();
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
        invalidateLayout();
    }

}
