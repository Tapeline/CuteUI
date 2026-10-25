package me.tapeline.cuteui;

import javax.microedition.lcdui.Image;
import java.io.IOException;

public class Sprites {

    public static Image BUTTON;
    public static Image BUTTON_PRESSED;

    public static void load() {
        try {
            BUTTON = Image.createImage("/sprites/btnlg.png");
            BUTTON_PRESSED = Image.createImage("/sprites/btnlgpressed.png");
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
    }

}
