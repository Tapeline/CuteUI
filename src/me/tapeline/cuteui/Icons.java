package me.tapeline.cuteui;

import javax.microedition.lcdui.Image;
import java.io.IOException;

public class Icons {

    public static Image FOLDER;
    public static Image MESSAGE;
    public static Image PREVIOUS;
    public static Image NEXT;
    public static Image SEARCH;
    public static Image SETTINGS;
    public static Image INFO;
    public static Image CHAT;
    public static Image CHAT16;
    public static Image MUSIC;
    public static Image MUSIC16;
    public static Image PHOTO;
    public static Image PHOTO16;
    public static Image VIDEO;
    public static Image VIDEO16;
    public static Image VOICE;
    public static Image VOICE16;
    public static Image REPLY16;
    public static Image DOWNLOAD;
    public static Image BIG_LOGO;
    public static Image LOCK;

    public static void load() {
        try {
            FOLDER = Image.createImage("/icons/folder.png");
            MESSAGE = Image.createImage("/icons/messaging.png");
            PREVIOUS = Image.createImage("/icons/previous.png");
            NEXT = Image.createImage("/icons/next.png");
            SEARCH = Image.createImage("/icons/search.png");
            SETTINGS = Image.createImage("/icons/settings.png");
            INFO = Image.createImage("/icons/info.png");
            CHAT = Image.createImage("/icons/chat.png");
            CHAT16 = Image.createImage("/icons/chat16.png");
            MUSIC = Image.createImage("/icons/music.png");
            MUSIC16 = Image.createImage("/icons/music16.png");
            PHOTO = Image.createImage("/icons/photo.png");
            PHOTO16 = Image.createImage("/icons/photo16.png");
            VIDEO = Image.createImage("/icons/video.png");
            VIDEO16 = Image.createImage("/icons/video16.png");
            VOICE = Image.createImage("/icons/voice.png");
            VOICE16 = Image.createImage("/icons/voice16.png");
            REPLY16 = Image.createImage("/icons/reply_message16.png");
            DOWNLOAD = Image.createImage("/icons/download.png");
            BIG_LOGO = Image.createImage("/icon.png");
            LOCK = Image.createImage("/icons/lock.png");
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
    }

}
