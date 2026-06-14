package me.tapeline.cuteui.components;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public class Label extends Component {

    private String text;
    private Font font;
    private int color;
    private String[] renderLines = null;

    public Label(String text) {
        this.text = text;
        this.font = Font.getDefaultFont();
        this.color = 0x000000;
    }

    public void measure(int maxW, int maxH) {
        if (text == null || text.length() == 0) {
            renderLines = new String[0];
            int h = font.getHeight();
            if (maxH != -1 && h > maxH) {
                h = maxH;
            }
            setMeasurementResult(0, h);
            return;
        }

        char[] chars = text.toCharArray();
        int measuredW = 0;
        int fontHeight = font.getHeight();
        java.util.Vector linesVector = new java.util.Vector();

        if (maxW == -1) {
            // UNBOUNDED WIDTH: No automatic wrapping, only wrap on explicit '\n'
            int lineStart = 0;
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == '\n') {
                    int lw = font.charsWidth(chars, lineStart, i - lineStart);
                    if (lw > measuredW) measuredW = lw;
                    linesVector.addElement(new String(chars, lineStart, i - lineStart));
                    lineStart = i + 1;
                }
            }
            // Add the remaining text after the last '\n'
            int len = chars.length - lineStart;
            if (len > 0 || chars[chars.length - 1] == '\n') {
                int lw = font.charsWidth(chars, lineStart, len);
                if (lw > measuredW) measuredW = lw;
                linesVector.addElement(new String(chars, lineStart, len));
            }

        } else {
            // BOUNDED WIDTH: Support word wrapping and explicit '\n'
            maxW -= 2 * (getMargin() + getPadding());
            int lineStart = 0;
            int currentW = 0;
            int lastSpace = -1;

            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == '\n') {
                    int lw = font.charsWidth(chars, lineStart, i - lineStart);
                    if (lw > measuredW) measuredW = lw;
                    linesVector.addElement(new String(chars, lineStart, i - lineStart));
                    lineStart = i + 1;
                    currentW = 0;
                    lastSpace = -1;
                } else {
                    if (chars[i] == ' ') {
                        lastSpace = i;
                    }

                    int cw = font.charWidth(chars[i]);

                    // Check if adding this character exceeds maxW
                    if (currentW + cw > maxW && i > lineStart) {
                        if (lastSpace > lineStart) {
                            // Word wrap: break at the last space found on this line
                            int lw = font.charsWidth(chars, lineStart, lastSpace - lineStart);
                            if (lw > measuredW) measuredW = lw;
                            linesVector.addElement(new String(chars, lineStart, lastSpace - lineStart));

                            lineStart = lastSpace + 1;

                            // Skip any subsequent consecutive spaces to prevent empty lines
                            while (lineStart < chars.length && chars[lineStart] == ' ') {
                                lineStart++;
                            }

                            // Step back so the loop increments naturally to the next character
                            i = lineStart - 1;
                            currentW = 0;
                            lastSpace = -1;
                        } else {
                            // Character wrap: word is too long, forced break before the current character
                            int lw = font.charsWidth(chars, lineStart, i - lineStart);
                            if (lw > measuredW) measuredW = lw;
                            linesVector.addElement(new String(chars, lineStart, i - lineStart));

                            lineStart = i;
                            currentW = cw;
                        }
                    } else {
                        currentW += cw;
                    }
                }
            }
            // Add the final line
            int len = chars.length - lineStart;
            if (len > 0 || chars[chars.length - 1] == '\n') {
                int lw = font.charsWidth(chars, lineStart, len);
                if (lw > measuredW) measuredW = lw;
                linesVector.addElement(new String(chars, lineStart, len));
            }

            // Constrain the measured width securely to max width
            if (measuredW > maxW) {
                measuredW = maxW;
            }
        }

        // Store generated strings into the field for the paint method
        renderLines = new String[linesVector.size()];
        for (int i = 0; i < linesVector.size(); i++) {
            renderLines[i] = (String) linesVector.elementAt(i);
        }

        // Calculate final height depending on the size of the renderLines array
        int measuredH = renderLines.length * fontHeight + (getMargin() + getPadding()) * 2;
        if (maxH != -1 && measuredH > maxH) {
            measuredH = maxH; // Clip if it overflows height bound constraint
        }

        setMeasurementResult(measuredW, measuredH);
    }

    public void paint(Graphics g) {
        if (renderLines == null) return;
        g.setColor(color);
        int x = getPadding() + getMargin() + getRectX();
        int y = getPadding() + getMargin() + getRectY();
        int fh = font.getHeight();
        for (int i = 0; i < renderLines.length; i++) {
            g.drawString(renderLines[i], x, y, Graphics.TOP | Graphics.LEFT);
            y += fh;
        }
        markPaintValid();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        invalidateLayout();
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
        invalidateLayout();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        invalidatePaint();
    }

}
