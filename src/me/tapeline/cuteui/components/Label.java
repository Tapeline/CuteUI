package me.tapeline.cuteui.components;

import me.tapeline.cuteui.Consts;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import java.util.Vector;

public class Label extends Component {

    private String text;
    private Font font;
    private int color;
    private String[] renderLines = null;
    private int alignment = Consts.ALIGN_LEFT;

    public Label(String text) {
        this.text = text;
        this.font = Font.getDefaultFont();
        this.color = 0x000000;
    }

    public void measure(int maxW, int maxH) {
        // Some slop based on my older algorithm of line-wrapping

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
        Vector linesVector = new Vector();

        if (maxW == -1) {
            int lineStart = 0;
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == '\n') {
                    int lw = font.charsWidth(chars, lineStart, i - lineStart);
                    if (lw > measuredW) measuredW = lw;
                    linesVector.addElement(new String(chars, lineStart, i - lineStart));
                    lineStart = i + 1;
                }
            }
            int len = chars.length - lineStart;
            if (len > 0 || chars[chars.length - 1] == '\n') {
                int lw = font.charsWidth(chars, lineStart, len);
                if (lw > measuredW) measuredW = lw;
                linesVector.addElement(new String(chars, lineStart, len));
            }

        } else {
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

                    if (currentW + cw > maxW && i > lineStart) {
                        if (lastSpace > lineStart) {
                            int lw = font.charsWidth(chars, lineStart, lastSpace - lineStart);
                            if (lw > measuredW) measuredW = lw;
                            linesVector.addElement(new String(chars, lineStart, lastSpace - lineStart));

                            lineStart = lastSpace + 1;

                            while (lineStart < chars.length && chars[lineStart] == ' ') {
                                lineStart++;
                            }

                            i = lineStart - 1;
                            currentW = 0;
                            lastSpace = -1;
                        } else {
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

            int len = chars.length - lineStart;
            if (len > 0 || chars[chars.length - 1] == '\n') {
                int lw = font.charsWidth(chars, lineStart, len);
                if (lw > measuredW) measuredW = lw;
                linesVector.addElement(new String(chars, lineStart, len));
            }

            if (measuredW > maxW) {
                measuredW = maxW;
            }
        }

        renderLines = new String[linesVector.size()];
        for (int i = 0; i < linesVector.size(); i++) {
            renderLines[i] = (String) linesVector.elementAt(i);
        }

        int measuredH = renderLines.length * fontHeight + (getMargin() + getPadding()) * 2;
        if (maxH != -1 && measuredH > maxH) {
            measuredH = maxH;
        }

        setMeasurementResult(measuredW, measuredH);
    }

    public void paint(Graphics g) {
        if (renderLines == null) return;
        g.setColor(color);
        int fh = font.getHeight();
        int y = getPadding() + getMargin() + getRectY();
        int x, anchor;
        if (alignment == Consts.ALIGN_CENTER) {
            x = getRectX() + getRectW() / 2;
            anchor = Graphics.TOP | Graphics.HCENTER;
        } else if (alignment == Consts.ALIGN_RIGHT) {
            x = getRectX() + getRectW();
            anchor = Graphics.TOP | Graphics.RIGHT;
        } else {
            x = getPadding() + getMargin() + getRectX();
            anchor = Graphics.TOP | Graphics.LEFT;
        }
        for (int i = 0; i < renderLines.length; i++) {
            g.drawString(renderLines[i], x, y, anchor);
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

    public int getAlignment() {
        return alignment;
    }

    public void setAlignment(int alignment) {
        this.alignment = alignment;
        invalidatePaint();
    }

}
