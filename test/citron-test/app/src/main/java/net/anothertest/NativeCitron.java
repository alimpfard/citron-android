package net.anothertest;

public class NativeCitron {
    public native boolean initialize(String ext_path);
    public native void destroy();
    public native String evaluate(String src);

    private boolean _error;

    public boolean is_error() {
        return _error;
    }

    public NativeCitron() {
        System.loadLibrary("citron");
    }
}


