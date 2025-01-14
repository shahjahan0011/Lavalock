package opencvutils;

import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.security.MessageDigest;
import java.util.Base64;

public class WebcamCapture {
    static {
        System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
    }

    private final VideoCapture camera;
    private final Mat frame;

    public WebcamCapture() {
        this.camera = new VideoCapture(0); // Open default webcam
        this.frame = new Mat();
    }

    public boolean isOpened() {
        return camera.isOpened();
    }

    public Image getCurrentFrame() {
        if (camera.isOpened()) {
            camera.read(frame); // Capture frame
            if (!frame.empty()) {
                Mat convertedFrame = new Mat();
                Imgproc.cvtColor(frame, convertedFrame, Imgproc.COLOR_BGR2BGRA); // Convert to BGRA

                WritableImage image = new WritableImage(convertedFrame.cols(), convertedFrame.rows());
                PixelWriter pw = image.getPixelWriter();
                byte[] buffer = new byte[(int) (convertedFrame.total() * convertedFrame.channels())];
                convertedFrame.get(0, 0, buffer);
                pw.setPixels(
                        0,
                        0,
                        convertedFrame.cols(),
                        convertedFrame.rows(),
                        PixelFormat.getByteBgraInstance(),
                        buffer,
                        0,
                        convertedFrame.cols() * 4
                );
                return image;
            }
        }
        return null;
    }

    public String calculateImageHash() {
        if (!frame.empty()) {
            try {
                // Get pixel data from the current frame
                byte[] buffer = new byte[(int) (frame.total() * frame.channels())];
                frame.get(0, 0, buffer);

                // Use SHA-256 to compute the hash
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hashBytes = md.digest(buffer);

                // Convert hash bytes to a hexadecimal string
                StringBuilder hexString = new StringBuilder();
                for (byte b : hashBytes) {
                    String hex = Integer.toHexString(0xff & b); // Convert byte to hex
                    if (hex.length() == 1) {
                        hexString.append('0'); // Add leading zero for single-digit hex
                    }
                    hexString.append(hex);
                }
                return hexString.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "Error";
    }


    public void release() {
        if (camera != null) {
            camera.release();
        }
    }
}
