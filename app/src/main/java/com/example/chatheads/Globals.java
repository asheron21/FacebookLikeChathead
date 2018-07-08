package com.example.chatheads;

import android.graphics.Bitmap;

public class Globals {


    private int testi;
    private Bitmap bmpscreenshot;
    private boolean readyToTakeShot;


    private static Globals instance = new Globals();
    // Getter-Setters
    public static Globals getInstance() {
        return instance;
    }

    public static void setInstance(Globals instance) {
        Globals.instance = instance;
    }

    private Globals() {

    }



    public Bitmap getScreenshot(){
        return bmpscreenshot;
    }

    public void setScreenshot(Bitmap bmp){
        this.bmpscreenshot = bmp.copy(bmp.getConfig(),true);
    }

    public boolean getReadyToTakeShot(){
        return readyToTakeShot;
    }

    public void setReadyToTakeShot(boolean b){
        readyToTakeShot = b;
    }


    public int getValue() {
        return testi;
    }

    public void setValue(int testi) {
        this.testi = testi;
    }

}
