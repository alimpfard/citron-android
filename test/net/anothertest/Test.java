package net.anothertest;
import net.anothertest.*; //NativeCitron;


public class Test {
    static NativeCitron nativeCitron = new NativeCitron();
    
    public static void main(String[] args) {
        nativeCitron.initialize();
        System.out.println(nativeCitron.evaluate("1024"));
        nativeCitron.destroy();
    }
}
