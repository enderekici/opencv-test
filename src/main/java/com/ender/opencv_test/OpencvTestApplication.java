package com.ender.opencv_test;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.opencv.imgproc.Imgproc.THRESH_BINARY;
import static org.opencv.imgproc.Imgproc.rectangle;
import static org.opencv.imgproc.Imgproc.threshold;

public class OpencvTestApplication {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello, OpenCV");
        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("\nRunning DetectFaceDemo");

        // Create a face detector from the cascade file in the resources
        // directory.
        String path = Paths.get("lbpcascade_frontalface.xml").toString();
        CascadeClassifier faceDetector = new CascadeClassifier(path);

        VideoCapture capture = new VideoCapture(0);
        if (!capture.isOpened()) {
            System.out.println("Did not connected to camera.");
        } else {
            System.out.println("Conected to camera: " + capture.toString());
        }


        int i = 0;
        boolean b=true;
        while (true&&b) {
            try {
                long l = System.nanoTime();
                i++;
                Mat image = new MatOfRect();
                capture.grab();
                capture.retrieve(image);

                MatOfRect faceDetections = new MatOfRect();
                faceDetector.detectMultiScale(image, faceDetections);

                Rect rect_Crop;
                // Draw a bounding box around each face.
                Rect[] array = faceDetections.toArray();
                for (int i1 = 0; i1 < array.length; i1++) {
                    Rect rect = array[i1];
                    //rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
                    String f = "pic/detect/" + i + "_" + i1 + faceDetections.toArray().length + "+detected.png";
                    //Imgcodecs.imwrite(f, image);
                    rect_Crop = new Rect(rect.x, rect.y, rect.width - 1, rect.height - 1);
                    Mat image_roi = new Mat(image, rect_Crop);


                    // Save the visualized detection.
                    //String filename = "pic/out/" + i + "_" + i1 + faceDetections.toArray().length + "+detected.png";
                    //System.out.println(String.format("Writing %s", filename));

                    Mat dst = new Mat();
                    Imgproc.cvtColor(image_roi, dst, Imgproc.COLOR_RGB2GRAY);

                    Size sz = new Size(250, 250);
                    Mat resizedBlackWhite = new Mat();
                    Imgproc.resize(dst, resizedBlackWhite, sz);

                    //Mat onlyBlackAndWhite = new Mat();
                    //threshold(resizedBlackWhite, onlyBlackAndWhite, 100, 255, THRESH_BINARY);


                    List<Integer> list = new ArrayList<>();

                    for (int q = 0; q < resizedBlackWhite.width(); q++) {
                        for (int p = 0; p < resizedBlackWhite.height(); p++) {
                            int i2 = (int) resizedBlackWhite.get(q, p)[0];
                            list.add(i2);
                        }
                    }

                    //Imgcodecs.imwrite(filename, resizedBlackWhite);
                    //System.out.println(list);
                    String collect = list.stream().map(String::valueOf).collect(Collectors.joining(","));
                    System.out.println(collect);


                }

                double v = (System.nanoTime() - l) / 1e6;
                System.out.println("time="+v);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
