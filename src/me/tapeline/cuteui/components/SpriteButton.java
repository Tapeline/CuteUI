package me.tapeline.cuteui.components;

import me.tapeline.cuteui.Sprites;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

public class SpriteButton extends Button {

    public SpriteButton(String text, int command) {
        super(text, command);
    }

    public void paint(Graphics g) {
        int xs = getRectX() + getMargin();
        int ys = getRectY() + getMargin();
        int xe = getRectX() + getRectW() - getMargin();
        int ye = getRectY() + getRectH() - getMargin();
        int cw = xe - xs;
        int ch = ye - ys;
        Image sprite = pressed? Sprites.BUTTON_PRESSED : Sprites.BUTTON;
        g.drawRegion(
            sprite, 0, 0, cw, ch, Sprite.TRANS_NONE,
            xs, ys, Graphics.TOP | Graphics.LEFT
        );
        g.drawRegion(
            sprite, 0, 40, cw, 8, Sprite.TRANS_NONE,
            xs, ye, Graphics.BOTTOM | Graphics.LEFT
        );
        g.drawRegion(
            sprite, 632, 0, 8, ch, Sprite.TRANS_NONE,
            xe, ys, Graphics.TOP | Graphics.RIGHT
        );
        g.drawRegion(
            sprite, 632, 40, 8, 8, Sprite.TRANS_NONE,
            xe, ye, Graphics.BOTTOM | Graphics.RIGHT
        );
        paintLines(g, 0);
    }

}
