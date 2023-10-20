

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

public class PixelDetection extends OpenCvPipeline {


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
    private static final Scalar
            lower_white_bounds = new Scalar(145, 145, 145, 255),
            upper_white_bounds = new Scalar(255, 255, 255, 255);

    // Color definitions
    private final Scalar WHITE = new Scalar(255, 255, 255);


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


    private double leftWhitePercent;
    private double centerWhitePercent;
    private double rightWhitePercent;

    // Running variable storing the parking position
    private volatile BackdropPosition position = BackdropPosition.LEFT;




    @Override
    public Mat processFrame(Mat input) {


        leftMat = createMatRect(input, leftMat_pointA, leftMat_pointB);
        centerMat = createMatRect(input, centerMat_pointA, centerMat_pointB);
        rightMat = createMatRect(input, rightMat_pointA, rightMat_pointB);

        leftWhitePercent = colorPercent(leftMat, lower_white_bounds, upper_white_bounds);
        centerWhitePercent = colorPercent(centerMat, lower_white_bounds, upper_white_bounds);
        rightWhitePercent = colorPercent(rightMat, lower_white_bounds, upper_white_bounds);

        double[] whitePercents = new double[] {leftWhitePercent, centerWhitePercent, rightWhitePercent};
        double highestWhitePercent = Arrays.stream(whitePercents).max().getAsDouble();


        // would do switch loop but "case must be constant expression"
        if (highestWhitePercent == leftWhitePercent)
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
        else if (highestWhitePercent == centerWhitePercent)
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
        else if (highestWhitePercent == rightWhitePercent)
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


    private double colorPercent(Mat blurredMat, Scalar lowerBound, Scalar upperBound) {
        // Remove all pixels that don't match a color between the color range specified and see
        // how many pixels there are remaining in the image, then find the percentage of these pixels to all of them
        Core.inRange(blurredMat, lowerBound, upperBound, blurredMat);
        return Core.countNonZero(blurredMat);
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

    public double getLeftWhitePercent() {
        return leftWhitePercent;
    }
    public double getCenterWhitePercent() {
        return centerWhitePercent;
    }
    public double getRightWhitePercent() {
        return rightWhitePercent;
    }


}

