

package org.firstinspires.ftc.teamcode;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.Arrays;

public class PixelDetectionThreeColors extends OpenCvPipeline {


    public enum BackdropPosition {
        LEFT,
        CENTER,
        RIGHT
    }


    // TODO: adjust points
    private static Point LEFT_MAT_TOPLEFT_ANCHOR_POINT = new Point(0, 0);
    private static Point CENTER_MAT_TOPLEFT_ANCHOR_POINT = new Point(0, 0);
    private static Point RIGHT_MAT_TOPLEFT_ANCHOR_POINT = new Point(0, 0);

    // Width and height for the bounding box
    // TODO: change size
    private static final int REGION_WIDTH = 30;
    private static final int REGION_HEIGHT = 40;

    // Lower and upper boundaries for colors
    // TODO: may need to adjust red and blue color bounds to mostly detect just the team prop and not the tape
    private static final Scalar
            lower_white_bounds = new Scalar(145, 145, 145, 255),
            upper_white_bounds = new Scalar(255, 255, 255, 255),
            upper_red_bounds = new Scalar(255, 0, 0, 255),
            lower_red_bounds = new Scalar(140, 28, 28, 255),
            upper_blue_bounds = new Scalar(0, 0, 255, 255),
            lower_blue_bounds = new Scalar(32, 41, 161, 255);

    // Color definitions
    private final Scalar
                  WHITE = new Scalar(255, 255, 255),
                  RED = new Scalar(255, 0, 0),
                  BLUE = new Scalar(0, 0, 255);



    private Mat leftMat = new Mat(REGION_WIDTH, REGION_HEIGHT, CvType.CV_16UC4);
    private Mat centerMat = new Mat(REGION_WIDTH, REGION_HEIGHT, CvType.CV_16UC4);
    private Mat rightMat = new Mat(REGION_WIDTH, REGION_HEIGHT, CvType.CV_16UC4);
    private Mat kernel = new Mat();


    // Anchor point definitions
    private final Point leftMat_pointA = generateMatPointA(LEFT_MAT_TOPLEFT_ANCHOR_POINT);
    private final Point leftMat_pointB = generateMatPointB(LEFT_MAT_TOPLEFT_ANCHOR_POINT);

    private final Point centerMat_pointA = generateMatPointA(CENTER_MAT_TOPLEFT_ANCHOR_POINT);
    private final Point centerMat_pointB = generateMatPointB(CENTER_MAT_TOPLEFT_ANCHOR_POINT);

    private final Point rightMat_pointA = generateMatPointA(RIGHT_MAT_TOPLEFT_ANCHOR_POINT);
    private final Point rightMat_pointB = generateMatPointB(RIGHT_MAT_TOPLEFT_ANCHOR_POINT);


    private double leftPercent;
    private double centerPercent;
    private double rightPercent;

    // Running variable storing the parking position
    private volatile BackdropPosition position = BackdropPosition.LEFT;




    @Override
    public Mat processFrame(Mat input) {


        leftMat = createMatRect(input, leftMat_pointA, leftMat_pointB);
        centerMat = createMatRect(input, centerMat_pointA, centerMat_pointB);
        rightMat = createMatRect(input, rightMat_pointA, rightMat_pointB);


        leftPercent = highestColorPercent(leftMat);
        centerPercent = highestColorPercent(centerMat);
        rightPercent = highestColorPercent(rightMat);

        double[] colorPercents = new double[] { leftPercent, centerPercent, rightPercent };
        double highestColorPercent = Arrays.stream(colorPercents).max().getAsDouble();


        // TODO: add the ability to find *what* color is most common in whichever area, useful for debugging
        if (highestColorPercent == leftPercent)
        {
            position = BackdropPosition.LEFT;
            Imgproc.rectangle(
                    input,
                    leftMat_pointA,
                    leftMat_pointB,
                    WHITE,
                    2
            );
        }
        else if (highestColorPercent == centerPercent)
        {
            position = BackdropPosition.CENTER;
            Imgproc.rectangle(
                    input,
                    centerMat_pointA,
                    centerMat_pointB,
                    WHITE,
                    2
            );
        }
        else if (highestColorPercent == rightPercent)
        {
            position = BackdropPosition.RIGHT;
            Imgproc.rectangle(
                    input,
                    rightMat_pointA,
                    rightMat_pointB,
                    WHITE,
                    2
            );
        }

        // Memory cleanup
        leftMat.release();
        centerMat.release();
        rightMat.release();
        kernel.release();

        return input;
    }




    // returns a specified rectangular section from a given mat
    // (in the processFrame method, the input mat is the entire video feed)
    private Mat createMatRect(Mat input, Point topLeft, Point bottomRight) {

        // blurs the mat for noise reduction (basically, remove the amount of extraneous and excess pixels
        // so we can easier locate the color we need)
        Imgproc.blur(input, input, new Size(5, 5));
        input = input.submat(new Rect(topLeft, bottomRight));

        // CHECK: morphology may not be necessary?
        kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
        Imgproc.morphologyEx(input, input, Imgproc.MORPH_CLOSE, kernel);
        return input;

    }


    // Remove all pixels that don't match a color between the color range specified and see
    // how many pixels there are remaining in the image, then find the percentage of these pixels to all of them
    private double colorPercent(Mat blurredMat, Scalar lowerBound, Scalar upperBound) {
        Core.inRange(blurredMat, lowerBound, upperBound, blurredMat);
        return Core.countNonZero(blurredMat);
    }


    // This calculates the highest percentage of a specific color found inside the mat.
    // This is for versatility and adaptability in case we can't use a team prop or something and have to
    // resort to the white pixels.
    // This searches for red, blue, and white pixels and calculates whichever color has the highest
    // corresponding pixels.
    // This could definitely be more versatile (as in passing in custom color bounds to the method),
    // but the implementation would be a bit too dirty and not really worthwhile.
    private double highestColorPercent(Mat blurredMat) {
        return Arrays.stream(new double[]{ colorPercent(blurredMat, lower_white_bounds, upper_white_bounds),
                colorPercent(blurredMat, lower_red_bounds, upper_red_bounds),
                colorPercent(blurredMat, lower_blue_bounds, upper_blue_bounds)
        }).max().getAsDouble();
    }




    // generates the top left corner of a detection mat based off of a predefined anchor point for that mat
    private Point generateMatPointA(Point matAnchorPoint) {
        return new Point(
                matAnchorPoint.x,
                matAnchorPoint.y);
    }

    // generates the bottom right corner of a detection mat based off of a predefined anchor point for that mat
    // and the predefined width and height of the bounding box
    private Point generateMatPointB(Point matAnchorPoint) {
        return new Point(
                matAnchorPoint.x + REGION_WIDTH,
                matAnchorPoint.y + REGION_HEIGHT);
    }




    // Returns an enum being the current position where the robot will park
    public BackdropPosition getPosition() {
        return position;
    }

    public double getLeftPercent() {
        return leftPercent;
    }
    public double getCenterPercent() {
        return centerPercent;
    }
    public double getRightPercent() {
        return rightPercent;
    }


}
