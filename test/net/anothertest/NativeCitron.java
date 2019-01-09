package net.anothertest;

public class NativeCitron {
    static {
        System.loadLibrary("citron");
    }
    public native boolean initialize();
    public native void destroy();
    public native String evaluate(String src);
}


