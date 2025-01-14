package opencvtests;

import org.opencv.core.Core;

public class OpenCVTest {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Load the OpenCV native library
    }

    public static void main(String[] args) {
        System.out.println("OpenCV version: " + Core.VERSION);
    }
}

